/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_privately_chat_util_FileUtils */

#ifndef _Included_org_privately_chat_util_FileUtils
#define _Included_org_privately_chat_util_FileUtils
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     org_privately_chat_util_FileUtils
 * Method:    getFileDescriptorOwner
 * Signature: (Ljava/io/FileDescriptor;)I
 */
JNIEXPORT jint JNICALL Java_org_privately_chat_util_FileUtils_getFileDescriptorOwner
  (JNIEnv *, jclass, jobject);

/*
 * Class:     org_privately_chat_util_FileUtils
 * Method:    createMemoryFileDescriptor
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_org_privately_chat_util_FileUtils_createMemoryFileDescriptor
  (JNIEnv *, jclass, jstring);

#ifdef __cplusplus
}
#endif
#endif
