#include "FrogBase64.h"

static const char *g_pCodes =
"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

static const char* base64_chars =
"ABCDEFGHIJKLMNOPQRSTUVWXYZ"
"abcdefghijklmnopqrstuvwxyz"
"0123456789+/";

static const unsigned char g_pMap[256] =
{
	255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	255, 255, 255, 255, 255, 255, 255, 62, 255, 255, 255, 63,
	52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 255, 255,
	255, 254, 255, 255, 255, 0, 1, 2, 3, 4, 5, 6,
	7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
	19, 20, 21, 22, 23, 24, 25, 255, 255, 255, 255, 255,
	255, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36,
	37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48,
	49, 50, 51, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	255, 255, 255, 255
};

// ���캯��
CFrogBase64::CFrogBase64()
{
}

// ��������
CFrogBase64::~CFrogBase64()
{
}

// ����
bool CFrogBase64::Encode(const unsigned char *pIn, unsigned long uInLen, unsigned char *pOut, unsigned long *uOutLen)
{
	unsigned long i, len2, leven;
	unsigned char *p;

	if (pOut == 0 || *uOutLen == 0)
		return false;

	//ASSERT((pIn != NULL) && (uInLen != 0) && (pOut != NULL) && (uOutLen != NULL));  

	len2 = ((uInLen + 2) / 3) << 2;
	if ((*uOutLen) < (len2 + 1)) return false;

	p = pOut;
	leven = 3 * (uInLen / 3);
	for (i = 0; i < leven; i += 3)
	{
		*p++ = g_pCodes[pIn[0] >> 2];
		*p++ = g_pCodes[((pIn[0] & 3) << 4) + (pIn[1] >> 4)];
		*p++ = g_pCodes[((pIn[1] & 0xf) << 2) + (pIn[2] >> 6)];
		*p++ = g_pCodes[pIn[2] & 0x3f];
		pIn += 3;
		if (i != 0 && i % (3*18) == 0)
		{
			*p++ = '\n';
		}
	}

	if (i < uInLen)
	{
		unsigned char a = pIn[0];
		unsigned char b = ((i + 1) < uInLen) ? pIn[1] : 0;
		unsigned char c = 0;

		*p++ = g_pCodes[a >> 2];
		*p++ = g_pCodes[((a & 3) << 4) + (b >> 4)];
		if (((i + 1) < uInLen))
		{
			*p++ = g_pCodes[((b & 0xf) << 2) + (c >> 6)];
		}
	}

	*p++ = '\n';
	*p = 0; // Append NULL byte  
	*uOutLen = p - pOut;
	return true;
}

//// ����
//bool CFrogBase64::Encode(const unsigned char *pIn, unsigned long uInLen, std::string& strOut)
//{
//	unsigned long i, len2, leven;
//	strOut = "";
//
//	//ASSERT((pIn != NULL) && (uInLen != 0) && (pOut != NULL) && (uOutLen != NULL));  
//
//	len2 = ((uInLen + 2) / 3) << 2;
//	//if((*uOutLen) < (len2 + 1)) return false;  
//
//	//p = pOut;  
//	leven = 3 * (uInLen / 3);
//	for (i = 0; i < leven; i += 3)
//	{
//		strOut += g_pCodes[pIn[0] >> 2];
//		strOut += g_pCodes[((pIn[0] & 3) << 4) + (pIn[1] >> 4)];
//		strOut += g_pCodes[((pIn[1] & 0xf) << 2) + (pIn[2] >> 6)];
//		strOut += g_pCodes[pIn[2] & 0x3f];
//		pIn += 3;
//	}
//
//	if (i < uInLen)
//	{
//		unsigned char a = pIn[0];
//		unsigned char b = ((i + 1) < uInLen) ? pIn[1] : 0;
//		unsigned char c = 0;
//
//		strOut += g_pCodes[a >> 2];
//		strOut += g_pCodes[((a & 3) << 4) + (b >> 4)];
//		strOut += ((i + 1) < uInLen) ? g_pCodes[((b & 0xf) << 2) + (c >> 6)] : '=';
//		strOut += '=';
//	}
//
//	//*p = 0; // Append NULL byte  
//	//*uOutLen = p - pOut;  
//	return true;
//}
//
//std::string CFrogBase64::base64_encode(unsigned char const* bytes_to_encode, unsigned int in_len, bool bLineGroup/* = true*/, bool bFillingEnd/* = false*/)
//{
//	std::string ret;
//	int i = 0;
//	int j = 0;
//	int nLen = 0;
//	unsigned char char_array_3[3];
//	unsigned char char_array_4[4];
//
//	while (in_len--) {
//		char_array_3[i++] = *(bytes_to_encode++);
//		if (i == 3) {
//			char_array_4[0] = (char_array_3[0] & 0xfc) >> 2;
//			char_array_4[1] = ((char_array_3[0] & 0x03) << 4) + ((char_array_3[1] & 0xf0) >> 4);
//			char_array_4[2] = ((char_array_3[1] & 0x0f) << 2) + ((char_array_3[2] & 0xc0) >> 6);
//			char_array_4[3] = char_array_3[2] & 0x3f;
//
//			for (i = 0; (i < 4); i++)
//			{
//				ret += base64_chars[char_array_4[i]];
//				nLen++;
//				if (bLineGroup && nLen % 76 == 0)
//				{
//					ret += '\n';
//				}
//			}
//			i = 0;
//		}
//	}
//
//	if (i)
//	{
//		for (j = i; j < 3; j++)
//			char_array_3[j] = '\0';
//
//		char_array_4[0] = (char_array_3[0] & 0xfc) >> 2;
//		char_array_4[1] = ((char_array_3[0] & 0x03) << 4) + ((char_array_3[1] & 0xf0) >> 4);
//		char_array_4[2] = ((char_array_3[1] & 0x0f) << 2) + ((char_array_3[2] & 0xc0) >> 6);
//		char_array_4[3] = char_array_3[2] & 0x3f;
//
//		for (j = 0; (j < i + 1); j++)
//		{
//			ret += base64_chars[char_array_4[j]];
//			nLen++;
//			if (bLineGroup && nLen % 76 == 0)
//			{
//				ret += '\n';
//			}
//		}
//
//		if (bFillingEnd)
//		{
//			// while ((i++ < 3))
//			//	ret += '=';
//		}
//	}
//	if (bLineGroup)
//	{
//		ret += '\n';
//	}
//	return ret;
//}
//
//// ����
//bool CFrogBase64::Decode(const std::string& strIn, unsigned char *pOut, unsigned long *uOutLen)
//{
//	unsigned long t, x, y, z;
//	unsigned char c;
//	unsigned long g = 3;
//
//	//ASSERT((pIn != NULL) && (uInLen != 0) && (pOut != NULL) && (uOutLen != NULL));  
//
//	for (x = y = z = t = 0; x < strIn.length(); x++)
//	{
//		c = g_pMap[strIn[x]];
//		if (c == 255) continue;
//		if (c == 254) { c = 0; g--; }
//
//		t = (t << 6) | c;
//
//		if (++y == 4)
//		{
//			if ((z + g) > *uOutLen) { return false; } // Buffer overflow  
//			pOut[z++] = (unsigned char)((t >> 16) & 255);
//			if (g > 1) pOut[z++] = (unsigned char)((t >> 8) & 255);
//			if (g > 2) pOut[z++] = (unsigned char)(t & 255);
//			y = t = 0;
//		}
//	}
//
//	*uOutLen = z;
//	return true;
//}
//
//std::string CFrogBase64::base64_decode(std::string const& encoded_string)
//{
//	int in_len = encoded_string.size();
//	int i = 0;
//	int j = 0;
//	int in_ = 0;
//	unsigned char char_array_4[4], char_array_3[3];
//	std::string ret;
//
//	while (in_len-- && (encoded_string[in_] != '=') && is_base64(encoded_string[in_])) {
//		char_array_4[i++] = encoded_string[in_]; in_++;
//		if (i == 4) {
//			for (i = 0; i < 4; i++)
//				char_array_4[i] = base64_chars.find(char_array_4[i]);
//
//			char_array_3[0] = (char_array_4[0] << 2) + ((char_array_4[1] & 0x30) >> 4);
//			char_array_3[1] = ((char_array_4[1] & 0xf) << 4) + ((char_array_4[2] & 0x3c) >> 2);
//			char_array_3[2] = ((char_array_4[2] & 0x3) << 6) + char_array_4[3];
//
//			for (i = 0; (i < 3); i++)
//				ret += char_array_3[i];
//			i = 0;
//		}
//	}
//
//	if (i) {
//		for (j = i; j < 4; j++)
//			char_array_4[j] = 0;
//
//		for (j = 0; j < 4; j++)
//			char_array_4[j] = base64_chars.find(char_array_4[j]);
//
//		char_array_3[0] = (char_array_4[0] << 2) + ((char_array_4[1] & 0x30) >> 4);
//		char_array_3[1] = ((char_array_4[1] & 0xf) << 4) + ((char_array_4[2] & 0x3c) >> 2);
//		char_array_3[2] = ((char_array_4[2] & 0x3) << 6) + char_array_4[3];
//
//		for (j = 0; (j < i - 1); j++) ret += char_array_3[j];
//	}
//
//	return ret;
//}

