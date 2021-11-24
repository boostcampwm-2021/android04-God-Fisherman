package com.android04.godfisherman.ui.main

import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintSet

class SwipeMotionLayoutWrapper(private val motionLayout: MotionLayout) {

    fun setupTransitionListener(
        transitionStartedCallback: ((view: MotionLayout?, startId: Int, endId: Int) -> Unit)?,
        transitionChangedCallback: ((view: MotionLayout?, startId: Int, endId: Int, progress: Float) -> Unit)?,
        transitionCompletedCallback: ((view: MotionLayout?, currentId: Int) -> Unit)?,
        transitionTriggerCallback: ((view: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) -> Unit)?
    ) {
        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
                transitionStartedCallback?.invoke(motionLayout, startId, endId)
            }

            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {
                transitionChangedCallback?.invoke(motionLayout, startId, endId, progress)
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                transitionCompletedCallback?.invoke(motionLayout, currentId)
            }

            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {
                transitionTriggerCallback?.invoke(motionLayout, triggerId, positive, progress)
            }

        })
    }

    fun transitionToState(constraintSetId: Int) {
        motionLayout.transitionToState(constraintSetId)
    }

    fun setProgress(progress: Float, afterProgress: () -> Unit) {
        motionLayout.progress = progress
        afterProgress()
    }

    fun setTransition(transitionId: Int) {
        motionLayout.setTransition(transitionId)
    }

    fun updateConstraint(
        constraintSetId: Int,
        constraintId: Int,
        updateCallback: (ConstraintSet.Constraint) -> Unit
    ) {
        val constraintSet = motionLayout.getConstraintSet(constraintSetId)
        val constraint = constraintSet.getConstraint(constraintId)
        updateCallback(constraint)
    }
}
