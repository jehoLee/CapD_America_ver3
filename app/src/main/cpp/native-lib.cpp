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


    for(int i=0;i<img_output.rows;i++) {

        for(int j=0;j<img_output.cols;j++) {
            //Vec3b pixel = img_output.at<Vec3b>(278+48*i,185 +168*j)
            int b = img_output.at<Vec3b>(i,j)[0];
            int g = img_output.at<Vec3b>(i,j)[1];
            int r = img_output.at<Vec3b>(i,j)[2];
            if ((b<250)&&(b>200)&&(g<250)&&(g>200)&&(r<250)&&(r>200))
                img_output.at<Vec3b>(i,j)=Vec3b(255,0,0);
            //if (img_output.at<Vec3b>(i,j) == Vec3b(255,255,255))
            //img_output.at<Vec3b>((i),(j))=Vec3b(0,0,0);
            //img_output.at<Vec3b>((225+75*i+1),(150+200*j+1))=Vec3b(0,0,0);
            //img_output.at<Vec3b>((225+75*i+1),(150+200*j))=Vec3b(0,0,0);
            //img_output.at<Vec3b>((225+75*i),(150+200*j+1))=Vec3b(0,0,0);

            //data.at<Vec3b>(i-1,j-1) = pixel;
            /*
            img_output.at<Vec3b>((278+48*i),(185 +168*j))=Vec3b(0,0,0);
            img_output.at<Vec3b>((278+48*i+1),(185 +168*j+1))=Vec3b(0,0,0);
            img_output.at<Vec3b>((278+48*i+1),(185 +168*j))=Vec3b(0,0,0);
            img_output.at<Vec3b>((278+48*i),(185 +168*j+1))=Vec3b(0,0,0);
             */

        }

        resize(img_output,img_output,Size(img_output.cols,img_output.rows),0,0,INTER_LINEAR);
    }



}
