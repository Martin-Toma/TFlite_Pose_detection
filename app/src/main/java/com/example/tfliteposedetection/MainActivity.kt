package com.example.tfliteposedetection

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.tfliteposedetection.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var poseDetector: PoseDetector
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    private val cameraProviderResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
            if (permissionGranted) {
                startCamAfterPermissionGranted()
            } else {
                Snackbar.make(
                    binding.root,
                    "The camera permission is required",
                    Snackbar.LENGTH_INDEFINITE
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)

        cameraProviderResult.launch(android.Manifest.permission.CAMERA)
    }

    fun startCamAfterPermissionGranted(){
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({

            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider = cameraProvider) //Bind camera provider

        }, ContextCompat.getMainExecutor(this))


        val localModel = LocalModel.Builder()
            .setAbsoluteFilePath("pose_detection.tflite")
            .build()

        val customPoseDetectorOptions = PoseDetectorOptions.Builder()
            .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
            .build()

        poseDetector = PoseDetection.getClient(customPoseDetectorOptions)
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build()

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        preview.setSurfaceProvider(binding.previewView.surfaceProvider)

        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280,720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), ImageAnalysis.Analyzer { imageProxy ->

            val rotationDegrees = imageProxy.imageInfo.rotationDegrees

            val image = imageProxy.image

            if(image != null) {
                val processImage = InputImage.fromMediaImage(image, rotationDegrees)
                    poseDetector
                        .process(processImage)
                        .addOnSuccessListener { pose ->
                            Log.d("Position", "In process")
                            // Get all PoseLandmarks. If no person was detected, the list will be empty
                            val allPoseLandmarks = pose.getAllPoseLandmarks()
                            
                            val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
                            val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
                            val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
                            val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)
                            val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
                            val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)
                            val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
                            val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
                            val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
                            val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)
                            val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
                            val rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)
                            val leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY)
                            val rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY)
                            val leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX)
                            val rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX)
                            val leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB)
                            val rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB)
                            val leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL)
                            val rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL)
                            val leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX)
                            val rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX)
                            val nose = pose.getPoseLandmark(PoseLandmark.NOSE)
                            val leftEyeInner = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_INNER)
                            val leftEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE)
                            val leftEyeOuter = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_OUTER)
                            val rightEyeInner = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_INNER)
                            val rightEye = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE)
                            val rightEyeOuter = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_OUTER)
                            val leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR)
                            val rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR)
                            val leftMouth = pose.getPoseLandmark(PoseLandmark.LEFT_MOUTH)
                            val rightMouth = pose.getPoseLandmark(PoseLandmark.RIGHT_MOUTH)

                            /*if(binding.parentLayout.childCount > 1) {
                                binding.parentLayout.removeViewAt(1)
                            }*/

                            // delete all new layers - layers with drawn points
                            while(binding.parentLayout.childCount > 1) {
                                binding.parentLayout.removeViewAt(1)
                            }

                            if(rightShoulder != null){
                                val x = rightShoulder.position.x
                                val y = rightShoulder.position.y
                                Log.d("Position", "Not sent x = ${x} y = ${y}")

                                val element = Draw(context = this,
                                    x, y, processImage.width, processImage.height)
                                binding.parentLayout.addView(element)
                            }

                            imageProxy.close()
                        }
                        .addOnFailureListener {
                            //Log.d("Position", "On failure")
                            imageProxy.close()
                        }
                //imageProxy.close()
            }
        })

        cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, imageAnalysis, preview)

    }
}