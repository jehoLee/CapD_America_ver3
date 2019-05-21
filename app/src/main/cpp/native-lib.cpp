#include <jni.h>
#include <opencv2/opencv.hpp>


using namespace cv;

extern "C"
JNIEXPORT void JNICALL
Java_ajou_com_skechip_UploadingActivity_imageprocessing(JNIEnv *env,
                                                        jobject instance,
                                                        jlong inputImage,
                                                        jlong outputImage) {



    Mat &img_input = *(Mat *) inputImage;

    Mat &img_output = *(Mat *) outputImage;




    img_output = img_input;


    resize(img_output,img_output,Size(1024,1024),0,0,INTER_LINEAR);



}
