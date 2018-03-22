## 0x00

上一篇我们提到使用SQLCipher对本地的数据进行加密。在保证数据安全的同时，我们又引入了一些新的问题，比如安装包体积的增加，数据存储过程中耗时增加，数据加密的粒度不可控等等。如果你接受不了这些问题，那我们还需寻找其他的加密方法。这里给大家推荐[Conceal][1]

## 0x01

[Conceal][1]是Facebook的一个开源项目，它可以高效的对大文件进行加密（当然对小数据加密也不是问题），同时对设备的性能和系统版本要求比较低。更具体的信息可参考[这里][2]。

> Conceal doesn't implement any crypto, but instead, it uses specific cryptographics algorithms from OpenSSL. Conceal attempts to manage memory efficiently between the native and Java heap. Conceal also uses fast modes like AES-GCM and HMAC-SHA1 by default.    
> OpenSSL is a very large library, and would increase the size of apps. Conceal ships with only a select number of encryption algorithms from OpenSSL which make it much smaller (85KB). 

## 0x02

### 引用

`implementation 'com.facebook.conceal:conceal:1.0.1@aar'`

### 关键代码

我们可以用其加密常见的数据类型，比如字符串，图片等。

- 字符串

``` java
    // 加密
    byte[] cipherText = mCrypto.encrypt(plainText, mEntity);
    // 解密
    byte[] plainText = mCrypto.decrypt(cipherText, mEntity);
```

- 大文件

``` java
    OutputStream out = mCrypto.getCipherOutputStream(fileOS, mEntity);
    int read = 0;
    byte[] buffer = new byte[1024];
    while ((read = sourceFile.read(buffer)) != -1) {
        out.write(buffer, 0, read);
    }
```

- 初始化

``` java
    public static final String ENTITY = "ttdevs";

    public static void init(Context context) {
        if (null == mCrypto) {
            mCrypto = new Crypto(new SharedPrefsBackedKeyChain(context), new SystemNativeCryptoLibrary());
            mEntity = new Entity(ENTITY);
        }
    }
```

以上三部分是我们使用Conceal的关键代码。首先，我们需要构造一个Crypto对象，这个对象需要传入两个参数，第一个是KeyChain，第二个是NativeCryptoLibrary。NativeCryptoLibrary我们使用系统默认的。KeyChain顾名思义，就是我们的加密key。例子中我们使用了SharedPrefsBackedKeyChain，通过查看源码，我们发现，它是将加密的key保存到SharedPreferences中的一个KeyChain的实现。

### 测试

接下来我们来看看Conceal的表现如何。我写了一个简单的测试代码，由于比较长，就不贴了，具体可以参考[这里][ttdevs]，主要有三个部分：

- 测试字符串加解密
- 测试加密数据的数据库读写
- 测试本地文件加解密

界面如下：

![测试界面](https://raw.githubusercontent.com/ttdevs/Demo/master/app/image-conceal-string.png)

- 字符串加解密

字符串的读写如上图，加解密字符串`Hello world!`耗时都在3ms左右，这个时间应该是我们可以接受的。

`PS:强势插入一个2B问题`
>细心的你可能会想如果我们不用数据库，只是加密几个简单的数据，能不能将密文直接写入SharedPreferences中呢？告诉你我也是这么想的，而且去测试了，但是死活不成功，不成功，不成功……
>我的思路是这样的：由于加密、加密结果、解密 三个操作参数都是byte，而SharedPreferences接受的参数是String类型，因此我们需要进行转换，`new String(bytes, "utf-8")` 和 `string.getBytes("utf-8")` ， 当你这么做的时候，你会发现解密死活不成功。经历过无数个不眠之夜后，终于恍然大悟，原来这么转换是错误的！原因很简单，一个utf-8编码汉字是2bytes，如果我们的加密结果不足2byte，我们强转时不足部分就会被补齐，当再转换回来的时候就和原来的不一一样了。最后怎么解决呢？也很简单，用Base64对byte数组进行编码，具体怎么做可以参考Conceal源码中的SharedPrefsBackedKeyChain。

- 数据库读写

这个时间就不做过多解读，数据库操作时间加加解密时间。

``` log
1456500607857: Begin transaction 
1456500607861: Insert: Hello world! 
1456500607865: Read: name:Hello world! token:Hello world! 
1456500607868: Read: name:Hello world! token:Hello world! 
1456500607871: Read: name:Hello world! token:Hello world! 
1456500607885: End transaction 
```

- 文件加解密

这个比较重要，如果我们有大文件加密的需求（比如前段时间微信的红包图片），对性能和效率要求就比较高了。

某一次加密数据：

![测试界面](https://raw.githubusercontent.com/ttdevs/Demo/master/app/image-conceal-file.png)

``` log
Encrpyt 1.png 1014KB 163ms
Encrpyt 2.jpg 2078KB 336ms
Encrpyt 3.jpg 3043KB 477ms
Encrpyt 4.JPG 3811KB 560ms
Encrpyt 5.JPG 4772KB 699ms
Encrpyt 10.apk 10268KB 1281ms
Encrpyt 10.gif 10421KB 671ms
Encrpyt 21.apk 20962KB 1323ms
```

某一次解密数据：

``` log
Decrpyt encrypt_1.png 1014KB 108ms
Decrpyt encrypt_2.jpg 2078KB 177ms
Decrpyt encrypt_3.jpg 3043KB 221ms
Decrpyt encrypt_4.JPG 3811KB 270ms
Decrpyt encrypt_5.JPG 4772KB 340ms
Decrpyt encrypt_10.apk 10268KB 732ms
Decrpyt encrypt_10.gif 10421KB 764ms
Decrpyt encrypt_21.apk 20962KB 1468ms
```

从上面的数据我们可以看到，加密多张大图并没有导致内存的增加，只是CPU使用率有所变化。加密1MB图片耗时160ms，加密20MB文件耗时1300ms多。解密数据的性能消耗类似。

## 0x03

### 分析

了解了Conceal的基本情况，我们来对比下上面提到的使用SQLCipher遇到的问题。首先，Conceal只有85KB，这个大小是我们完全可以接受的，然后就是加密粒度，无论是数据库还是文件，我们都可以只真对需要的那部分加密，最后即使你选择将文件存入数据库，它的时间也只有正常数据库操作时间加Conceal的加解密时间。综上所述，Conceal应该是一个不错的选择。

### 总结

我们再来想想这两篇文章中提到的加密方法：无论你选择哪一种，由于他们都采用了对称加密算法，虽然数据部分加密了，但是我们却需要维护一个秘钥，这个秘钥放在那里？SharedPreferences？数据库？本地文件？貌似进入了一个死循环，问题又回到了起点。分析了这么多，如果没有办法解决秘钥的问题，也都是白搭。怎么办？下一篇在进一步分析。敬请期待。

`PS: 所有测试机器 魅族 MX4PRO 性能均衡模式`

-----------
[1]: https://github.com/facebook/conceal
[2]: https://facebook.github.io/conceal/
[ttdevs]: https://github.com/ttdevs/Demo