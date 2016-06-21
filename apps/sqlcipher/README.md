SQLCipher
============

# 前情回顾
## 0x00 

先纪念下，从今天开始，又开始写博客啦啦啦啦。话说基本上一年多没好好写博客了。回想起来，和之前写博客时相比，还是差距好大。好吧，废话不说了，进入正题。

首先，开篇打算跟大家分享下你遇到的或者将来可能遇到的问题：本地数据的安全问题。多数情况，我们可能没有去考虑app的本地数据安全问题，一来公司没这样的要求，二来我们可能也没有非常需要保密的数据。那现在思考一下为将来做储备。

面试的时候，可能常常会被问到这样一个问题：Android的数据存储有哪几种方式？ 对于这个问题，[Android的官方文档][1] 有回答：

- Shared Preferences
- Internal Storage
- External Storage
- SQLite Databases
- Network Connection

这几种方式就不用多说了。第一种以key-value的方式存储在我们的设备内部；第二种第三种简单理解为文件存储；第四种本地数据库存储（这四种都属本地存储）。最后一种方式（暂且算是一种方式吧）不在我们考虑范围之内。现在假设一个最坏的环境：设备已经root，任何本地数据都可以被直接或者间接拿到。这时候我们自然想到的就是对我们的数据进行加密。

# 本地数据库加密

## 0x01 

以上是开场，下面进入第一部分：本地数据库加密。
先回顾下本地数据库，root以后，我们可以在下面的位置找到它：`/data/data/you.package/databases`，例如我们demo的数据库：
``` shell
root@t03g:/data/data/com.ttdevs.demo/databases # ls
normal_user.db
normal_user.db-journal
user.db
root@t03g:/data/data/com.ttdevs.demo/databases #
``` 
知道了它的位置，接下来我们就可以直接将这些数据库文件拷贝出来，然后使用SQLite浏览器浏览。
我们选用网上一个开源的方案：[SQLCipher][2]（[GitHub地址][3]）。SQLCipher是在SQLite的基础上封装了加密功能，使用上和直接使用SQLite基本相同。

## 0x02

首先，按照说明，我们引入SQLCipher：
``` gradle
compile 'net.zetetic:android-database-sqlcipher:3.3.1-2@aar'
```

接下来就是编写我们的数据库代码了。先创建一个DatabaseHelper：
``` java
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE = "user_info";
    public static final String NAME = "name";
    public static final String AGE = "age";
    public static final String ICON = "icon";
    public static final String TOKEN = "token";

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE +
                " (_id integer primary key autoincrement, " +
                NAME + " text, " +
                AGE + " integer, " +
                ICON + " blob, " +
                TOKEN + " text not null);";
        Log.d("EventsData", "onCreate: " + sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            // TODO
        }
    }
}
```

`注意，此处的SQLiteOpenHelper和SQLiteDatabase都来自net.sqlcipher.database包。`

为了便于对比，我又写了一个相同逻辑使用`android.database.sqlite.SQLiteOpenHelper`的Helper作对比。接下来使用编写数据库操作逻辑。使用中SQLiteCipher区别是打开数据库的时候需要传入用于加密的KEY。下面是测试代码：
``` java
public class SqlcipherActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = ">>>>>";
    private static final String CIPHER_KEY = "123abc_!@#$%^&*";
    private DatabaseOpenHelper mOpenHelper;
    private NormalDatabaseOpenHelper mNormalHelper;
...
    private void writeData(boolean isCipher) {
        if (isCipher) {
            log("Cipher:101:" + System.currentTimeMillis());
            SQLiteDatabase db = mOpenHelper.getWritableDatabase(PASSWORD_KEY);
            log("Cipher:102:" + System.currentTimeMillis());
            String name = "Cipher" + (++mIndex);
            ContentValues values = new ContentValues();
            values.put(DatabaseOpenHelper.NAME, name);
            values.put(DatabaseOpenHelper.AGE, mIndex);
            values.put(DatabaseOpenHelper.TOKEN, PASSWORD_KEY);
            values.put(DatabaseOpenHelper.ICON, name.getBytes());
            log("Cipher:11:" + System.currentTimeMillis());
            try {
                db.beginTransaction();
                db.insert(DatabaseOpenHelper.TABLE, null, values);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
            log("Cipher:12:" + System.currentTimeMillis());
            db.close();
        } else {
            ...
        }
    }

    private void readData(boolean isCipher) {
        if (isCipher) {
            SQLiteDatabase db = mOpenHelper.getReadableDatabase(PASSWORD_KEY);
            Cursor cursor = db.query(DatabaseOpenHelper.TABLE, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.NAME));
                int age = cursor.getInt(cursor.getColumnIndex(DatabaseOpenHelper.AGE));
                String token = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.TOKEN));
                String result = String.format("Cipher>>>> name:%s, age:%d, token:%s", name, age, token);
                log(result);
            }
            db.close();
        } else {
            android.database.sqlite.SQLiteDatabase ndb = mNormalHelper.getReadableDatabase();
            android.database.Cursor cursor = ndb.query(NormalDatabaseOpenHelper.TABLE, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.NAME));
                int age = cursor.getInt(cursor.getColumnIndex(DatabaseOpenHelper.AGE));
                String token = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.TOKEN));
                String result = String.format("Normal>>>> name:%s, age:%d, token:%s", name, age, token);
                log(result);
            }
            ndb.close();
        }
    }
    ...
}
```
好了，跑一下我们的测试代码，得到下面的结果：
``` log
02-19 13:49:41.596 24884-24884/com.ttdevs.demo E/>>>>>: >>>>> Cipher >>>>>
02-19 13:49:41.596 24884-24884/com.ttdevs.demo E/>>>>>: Cipher:101:1455860981597
02-19 13:49:41.971 24884-24884/com.ttdevs.demo E/>>>>>: Cipher:102:1455860981974
02-19 13:49:41.971 24884-24884/com.ttdevs.demo E/>>>>>: Cipher:11:1455860981974
02-19 13:49:41.981 24884-24884/com.ttdevs.demo E/>>>>>: Cipher:12:1455860981983
02-19 13:49:42.201 24884-24884/com.ttdevs.demo E/>>>>>: Cipher>>>> name:Cipher1, age:1, token:123abc_!@#$%^&*
02-19 13:49:42.201 24884-24884/com.ttdevs.demo I/Choreographer: Skipped 35 frames!  The application may be doing too much work on its main thread.
02-19 13:49:49.471 24884-24884/com.ttdevs.demo E/>>>>>: >>>>> Noremal >>>>>
02-19 13:49:49.476 24884-24884/com.ttdevs.demo E/>>>>>: Normal:101:1455860989477
02-19 13:49:49.521 24884-24884/com.ttdevs.demo E/>>>>>: Normal:102:1455860989525
02-19 13:49:49.521 24884-24884/com.ttdevs.demo E/>>>>>: Normal:11:1455860989525
02-19 13:49:49.531 24884-24884/com.ttdevs.demo E/>>>>>: Normal:12:1455860989535
02-19 13:49:49.541 24884-24884/com.ttdevs.demo E/>>>>>: Normal>>>> name:Normal2, age:2, token:123abc_!@#$%^&*
```
通过简单的对比，我们发现，SQLCipher在打开写数据库的时候花费了375ms，时间大概是SQLite 45ms的8~9倍，所以我们在log中看到丢帧的提示。这就提示我们，简单的数据库操作，使用SQLite我们也可能需要考虑将其放在新的线程中。当然，这个测试对比不够完善，比如大文件写入，大量数据插入，事务等等，因此还需要更多、更详细的测试来检测SQLCipher的性能等，这里就暂不讨论了。

跑完上面的代码，我们来看看我们本地数据库中都创建了哪些东西：
``` shell
root@t03g:/data/data/com.ttdevs.demo/databases # ls
normal_user.db
normal_user.db-journal
user.db
root@t03g:/data/data/com.ttdevs.demo/databases #
```
我们把这两个db文件拷贝到我们的电脑，看看里面有哪些东西。这里推荐一个工具：[sqlitebrowser][4]。打开他们的网站你会发现它有两个版本的：Standard和SQLCipher的。是的，你没看错，就是针对SQLCipher的。如果你使用Standard版本，你会发现无法打开我们加密后的user.db（当然你也可是试试其他方法）。使用SQLCipher版的，打开之前会要求输入我们加密的KEY，就是我们上面的`123abc_!@#$%^&*`。这时候我们就可以看到原始的数据了，如下图：
![Browser SQLCipher Data](http://img.blog.csdn.net/20160219151008300)


## 0x03

到这里，我们基本上已经见到了SQLCipher的使用。还没有结束，再来看一个问题：在创建项目开始，我尝试跑了下项目，apk的大小是1.4MB，在Gradle中引入SQLCipher之后，体积一下子飙到了8.7MB。呵呵呵~~ 这个体积的增加还是值得我们慎重的思考一下下的。

## 0x04

说了这么多，我们可以得到这样一个结论，使用SQLiteCipher可以对我们客户端的本地数据库进行加密，但是我们需要自己存一个加密KEY，如果我们能保存好这个KEY，那我们的数据基本是安全的。但是同时，我们可能需要考虑数据库加密过程中引入的新的问题，比如：apk体积的暴增，加密对性能的影响，加密KEY的保存等等。这些问题考虑好之后，我们就可以决定是否引入SQLCipher。

如果你对引入SQLiteCiper引起的新问题心存芥蒂，那接下来给大家推荐下一种方法。敬请期待：[Android本地数据安全尝试(中)][5]

PS：[Github Demo][6]

------
[1]:http://developer.android.com/guide/topics/data/data-storage.html
[2]:https://www.zetetic.net/sqlcipher/open-source/
[3]:https://github.com/sqlcipher/android-database-sqlcipher
[4]:http://sqlitebrowser.org/
[5]:http://blog.csdn.net/ttdevs/
[6]:https://github.com/ttdevs/Demo
