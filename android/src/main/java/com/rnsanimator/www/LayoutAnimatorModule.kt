// rns-animator\android\src\main\java\com\rnsanimator\www\LayoutAnimatorModule.kt
package com.rnsanimator.www

import android.transition.*
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import com.facebook.react.bridge.*
import com.facebook.react.uimanager.UIManagerModule
import android.view.Gravity
import androidx.interpolator.view.animation.FastOutSlowInInterpolator


class LayoutAnimatorModule(private val reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String = "LayoutAnimator"

    // Basic animations
    @ReactMethod
    fun slideOnReorder(duration: Int, easing: String, promise: Promise) {
        performTransition(ChangeBounds(), duration, easing, promise)
    }
    

    @ReactMethod
    fun fadeIn(duration: Int, easing: String, promise: Promise) {
        performTransition(Fade(Fade.IN), duration, easing, promise)
    }

    @ReactMethod
    fun fadeOut(duration: Int, easing: String, promise: Promise) {
        performTransition(Fade(Fade.OUT), duration, easing, promise)
    }

    // Advanced animations
    @ReactMethod
    fun explode(duration: Int, easing: String, promise: Promise) {
        performTransition(Explode(), duration, easing, promise)
    }

    @ReactMethod
    fun slideFromLeft(duration: Int, easing: String, promise: Promise) {
        performTransition(Slide(Gravity.LEFT), duration, easing, promise)
    }

    @ReactMethod
    fun slideFromRight(duration: Int, easing: String, promise: Promise) {
        performTransition(Slide(Gravity.RIGHT), duration, easing, promise)
    }

    @ReactMethod
    fun scaleUp(duration: Int, easing: String, promise: Promise) {
        val transition = TransitionSet()
            .addTransition(ChangeBounds())
            .addTransition(ChangeTransform())
            .addTransition(ChangeClipBounds())
        performTransition(transition, duration, easing, promise)
    }

    // Complex animation sequences
    @ReactMethod
    fun magicMove(duration: Int, easing: String, promise: Promise) {
        val transition = TransitionSet()
            .addTransition(ChangeBounds())
            .addTransition(ChangeTransform())
            .addTransition(ChangeClipBounds())
            .addTransition(ChangeImageTransform())
        performTransition(transition, duration, easing, promise)
    }

    @ReactMethod
    fun fanOut(duration: Int, easing: String, promise: Promise) {
        val transition = AutoTransition().apply {
            ordering = TransitionSet.ORDERING_TOGETHER
            addTransition(ChangeBounds())
            addTransition(Fade(Fade.IN))
        }
        performTransition(transition, duration, easing, promise)
    }

    // Spring physics animation
    @ReactMethod
    fun spring(duration: Int, damping: Float, stiffness: Float, promise: Promise) {
        val activity = currentActivity ?: run {
            promise.reject("NO_ACTIVITY", "Activity is null")
            return
        }

        activity.runOnUiThread {
            val rootView = activity.findViewById<ViewGroup>(android.R.id.content)
            val transition = ChangeBounds().apply {
                this.duration = duration.toLong()
                interpolator = SpringInterpolator(damping, stiffness)
            }

            try {
                TransitionManager.beginDelayedTransition(rootView, transition)
                promise.resolve(true)
            } catch (e: Exception) {
                promise.reject("TRANSITION_ERROR", e.message)
            }
        }
    }

    // Shared transition between two views
    @ReactMethod
    fun sharedTransition(
        fromViewTag: Int,
        toViewTag: Int,
        duration: Int,
        easing: String,
        promise: Promise
    ) {
        val activity = currentActivity ?: run {
            promise.reject("NO_ACTIVITY", "Activity is null")
            return
        }

        reactContext.getNativeModule(UIManagerModule::class.java)?.let { uiManager ->
            activity.runOnUiThread {
                val fromView = uiManager.resolveView(fromViewTag)
                val toView = uiManager.resolveView(toViewTag)

                if (fromView == null || toView == null) {
                    promise.reject("INVALID_VIEW", "One or both views not found")
                    return@runOnUiThread
                }

                val transition = TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(ChangeTransform())
                    .addTransition(ChangeClipBounds())
                    .apply {
                        this.duration = duration.toLong()
                        interpolator = getInterpolator(easing)
                        addTarget(fromView)
                        addTarget(toView)
                    }

                try {
                    TransitionManager.beginDelayedTransition(
                        fromView.parent as ViewGroup,
                        transition
                    )
                    promise.resolve(true)
                } catch (e: Exception) {
                    promise.reject("TRANSITION_ERROR", e.message)
                }
            }
        } ?: promise.reject("NO_UIMANAGER", "UIManager not available")
    }

    // Core transition performer
    private fun performTransition(
        transition: Transition,
        duration: Int,
        easing: String,
        promise: Promise
    ) {
        val activity = currentActivity ?: run {
            promise.reject("NO_ACTIVITY", "Activity is null")
            return
        }

        activity.runOnUiThread {
            val rootView = activity.findViewById<ViewGroup>(android.R.id.content)
            transition.apply {
                this.duration = duration.toLong()
                interpolator = getInterpolator(easing)
            }

            try {
                TransitionManager.beginDelayedTransition(rootView, transition)
                promise.resolve(true)
            } catch (e: Exception) {
                promise.reject("TRANSITION_ERROR", e.message)
            }
        }
    }

    // Enhanced interpolator system
    private fun getInterpolator(type: String): Interpolator {
        return when (type) {
            "ease-in" -> AccelerateInterpolator()
            "ease-out" -> DecelerateInterpolator()
            "ease-in-out" -> AccelerateDecelerateInterpolator()
            "linear" -> LinearInterpolator()
            "bounce" -> BounceInterpolator()
            "anticipate" -> AnticipateInterpolator()
            "overshoot" -> OvershootInterpolator()
            "anticipate-overshoot" -> AnticipateOvershootInterpolator()
            "fast-out-slow-in" -> FastOutSlowInInterpolator()
            else -> AccelerateDecelerateInterpolator()
        }
    }

    // Custom spring interpolator
    private inner class SpringInterpolator(
        private val damping: Float,
        private val stiffness: Float
    ) : Interpolator {
       override fun getInterpolation(input: Float): Float {
    return (-Math.pow(Math.E, (-damping * input).toDouble()) *
            Math.cos((stiffness * input).toDouble()) + 1.0).toFloat()
}

    }
}