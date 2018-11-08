package com.example.andyelsbury.digtalfacemoana;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.view.SurfaceHolder;
import android.view.WindowInsets;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Digital watch face with seconds. In ambient mode, the seconds aren't displayed. On devices with
 * low-bit ambient mode, the text is drawn without anti-aliasing in ambient mode.
 * <p>
 * Important Note: Because watch face apps do not have a default Activity in
 * their project, you will need to set your Configurations to
 * "Do not launch Activity" for both the Wear and/or Application modules. If you
 * are unsure how to do this, please review the "Run Starter project" section
 * in the Google Watch Face Code Lab:
 * https://codelabs.developers.google.com/codelabs/watchface/index.html#0
 */
public class MoanaDigital extends CanvasWatchFaceService {
    private static final Typeface NORMAL_TYPEFACE =
            Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);

    /**
     * Update rate in milliseconds for interactive mode. Defaults to one second
     * because the watch face needs to update seconds in interactive mode.
     */
    private static final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.SECONDS.toMillis(1);

    /**
     * Handler message id for updating the time periodically in interactive mode.
     */
    private static final int MSG_UPDATE_TIME = 0;

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    private static class EngineHandler extends Handler {
        private final WeakReference<MoanaDigital.Engine> mWeakReference;

        public EngineHandler(MoanaDigital.Engine reference) {
            mWeakReference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            MoanaDigital.Engine engine = mWeakReference.get();
            if (engine != null) {
                switch (msg.what) {
                    case MSG_UPDATE_TIME:
                        engine.handleUpdateTimeMessage();
                        break;
                }
            }
        }
    }

    private class Engine extends CanvasWatchFaceService.Engine {

        private final Handler mUpdateTimeHandler = new EngineHandler(this);
        private Calendar mCalendar;
        private final BroadcastReceiver mTimeZoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mCalendar.setTimeZone(TimeZone.getDefault());
                invalidate();
            }
        };
        private boolean mRegisteredTimeZoneReceiver = false;
        private float mXOffset;
        private float mYOffset;
        private float mXOffsetCal;
        private float mYOffsetCal;
        private Paint mBackgroundPaint;
        private Paint mTextPaint;
        private Paint mTextPaintCal;

        /**
         * Whether the display supports fewer bits for each color in ambient mode. When true, we
         * disable anti-aliasing in ambient mode.
         */
        private boolean mLowBitAmbient;
        private boolean mBurnInProtection;
        private boolean mAmbient;
        private Bitmap mBackgroundBitmap;
        private Bitmap mGrayBackgroundBitmap;

        private int backgroundResId1 = R.drawable.frame_01;
        private int backgroundResId2 = R.drawable.frame_02;
        private int backgroundResId3 = R.drawable.frame_03;
        private int backgroundResId4 = R.drawable.frame_04;
        private int backgroundResId5 = R.drawable.frame_05;
        private int backgroundResId6 = R.drawable.frame_06;
        private int backgroundResId7 = R.drawable.frame_07;
        private int backgroundResId8 = R.drawable.frame_08;
        private int backgroundResId9 = R.drawable.frame_09;
        private int backgroundResId10 = R.drawable.frame_10;
        private int backgroundResId11 = R.drawable.frame_11;
        private int backgroundResId12 = R.drawable.frame_12;
        private int backgroundResId13 = R.drawable.frame_13;
        private int backgroundResId14 = R.drawable.frame_14;
        private int backgroundResId15 = R.drawable.frame_15;
        private int backgroundResId16 = R.drawable.frame_16;
        private int backgroundResId17 = R.drawable.frame_17;
        private int backgroundResId18 = R.drawable.frame_18;
        private int backgroundResId19 = R.drawable.frame_19;
        private int backgroundResId20 = R.drawable.frame_20;
        private int backgroundResId21 = R.drawable.frame_21;
        private int backgroundResId22 = R.drawable.frame_22;
        private int backgroundResId23 = R.drawable.frame_23;
        private int backgroundResId24 = R.drawable.frame_24;
        private int backgroundResId25 = R.drawable.frame_25;
        private int backgroundResId26 = R.drawable.frame_26;
        private int backgroundResId27 = R.drawable.frame_27;
        private int backgroundResId28 = R.drawable.frame_28;
        private int backgroundResId29 = R.drawable.frame_29;
        private int backgroundResId30 = R.drawable.frame_30;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(MoanaDigital.this)
                    .setAcceptsTapEvents(true)
                    .build());

            mCalendar = Calendar.getInstance();

            Resources resources = MoanaDigital.this.getResources();
            mYOffset = resources.getDimension(R.dimen.digital_y_offset);
            mYOffsetCal = resources.getDimension(R.dimen.digital_y_offset_cal);

            // Initializes background.
            mBackgroundPaint = new Paint();
            mBackgroundPaint.setColor(ContextCompat.getColor(getApplicationContext(), R.color.background));

            /*
             * Toggle the backgroundResIds to see
             * the change of colors due to palette doing its magic.
             */
            final int backgroundResId = R.drawable.frame_00;

            mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId);

            // Initializes Watch Face.
            mTextPaint = new Paint();
            mTextPaint.setTypeface(NORMAL_TYPEFACE);
            mTextPaint.setAntiAlias(true);
            mTextPaint.setColor(ContextCompat.getColor(getApplicationContext(), R.color.digital_text));

            // Initializes Watch Face.
            mTextPaintCal = new Paint();
            mTextPaintCal.setTypeface(NORMAL_TYPEFACE);
            mTextPaintCal.setAntiAlias(true);
            mTextPaintCal.setColor(ContextCompat.getColor(getApplicationContext(), R.color.digital_text));
        }

        @Override
        public void onDestroy() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            super.onDestroy();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (visible) {
                registerReceiver();

                // Update time zone in case it changed while we weren't visible.
                mCalendar.setTimeZone(TimeZone.getDefault());
                invalidate();
            } else {
                unregisterReceiver();
            }

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
        }

        private void registerReceiver() {
            if (mRegisteredTimeZoneReceiver) {
                return;
            }

            mRegisteredTimeZoneReceiver = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            MoanaDigital.this.registerReceiver(mTimeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return;
            }

            mRegisteredTimeZoneReceiver = false;
            MoanaDigital.this.unregisterReceiver(mTimeZoneReceiver);
        }

        @Override
        public void onApplyWindowInsets(WindowInsets insets) {
            super.onApplyWindowInsets(insets);

            // Load resources that have alternate values for round watches.
            Resources resources = MoanaDigital.this.getResources();
            boolean isRound = insets.isRound();
            mXOffset = resources.getDimension(isRound
                    ? R.dimen.digital_x_offset_round : R.dimen.digital_x_offset);
            mXOffsetCal = resources.getDimension(isRound
                    ? R.dimen.digital_x_offset_round_cal : R.dimen.digital_x_offset_cal);
            float textSize = resources.getDimension(isRound
                    ? R.dimen.digital_text_size_round : R.dimen.digital_text_size);
            float textSizeCal = resources.getDimension(isRound
                    ? R.dimen.digital_text_size_round_cal : R.dimen.digital_text_size_cal);

            mTextPaint.setTextSize(textSize);
            mTextPaintCal.setTextSize(textSizeCal);
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
            mLowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
            mBurnInProtection = properties.getBoolean(PROPERTY_BURN_IN_PROTECTION, false);
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);

            mAmbient = inAmbientMode;
            if (mLowBitAmbient) {
                mTextPaint.setAntiAlias(!inAmbientMode);
                mTextPaintCal.setAntiAlias(!inAmbientMode);
            }

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
        }

        /**
         * Captures tap event (and tap type) and toggles the background color if the user finishes
         * a tap.
         */
        @Override
        public void onTapCommand(int tapType, int x, int y, long eventTime) {
            switch (tapType) {
                case TAP_TYPE_TOUCH:
                    // The user has started touching the screen.

                    break;
                case TAP_TYPE_TOUCH_CANCEL:
                    // The user has started a different gesture or otherwise cancelled the tap.
                    break;
                case TAP_TYPE_TAP:
                    // The user has completed the tap gesture.
                    // TODO: Add code to handle the tap gesture.
                    /*
                    if (x > 210) {
                        // add button
                        Toast.makeText(getApplicationContext(), R.string.message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.message2, Toast.LENGTH_SHORT).show();
                    }
                    */
                    break;
            }
            invalidate();
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            // Draw the background.
            if (isInAmbientMode()) {
                canvas.drawColor(Color.BLACK);
            } else {
                //canvas.drawRect(0, 0, bounds.width(), bounds.height(), mBackgroundPaint);
                canvas.drawBitmap(mBackgroundBitmap, 0, 0, mBackgroundPaint);
            }

            // Draw H:MM in ambient mode or H:MM:SS in interactive mode.
            long now = System.currentTimeMillis();
            mCalendar.setTimeInMillis(now);

            float seconds = (mCalendar.get(Calendar.SECOND) + mCalendar.get(Calendar.MILLISECOND) / 1000f);

            if (seconds >= 0.30) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId1);
                invalidate();
            }
            if (seconds >= 0.40) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId1);
                invalidate();
            }
            if (seconds >= 0.50) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId1);
                invalidate();
            }
            if (seconds >= 0.60) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId1);
                invalidate();
            }
            if (seconds >= 0.70) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId1);
                invalidate();
            }
            if (seconds >= 0.80) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId1);
                invalidate();
            }
            if (seconds >= 0.90) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId1);
                invalidate();
            }
            if (seconds >= 1.00) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId1);
                invalidate();
            }
            if (seconds >= 1.100) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId1);
                invalidate();
            }
            if (seconds >= 1.200) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId1);
                invalidate();
            }
            if (seconds >= 1.300) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId1);
                invalidate();
            }
            if (seconds >= 1.400) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId1);
                invalidate();
            }
            if (seconds >= 1.500) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId1);
                invalidate();
            }
            if (seconds >= 1.600) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId2);
                invalidate();
            }
            if (seconds >= 1.700) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId3);
                invalidate();
            }
            if (seconds >= 1.800) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId4);
                invalidate();
            }
            if (seconds >= 1.900) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId5);
                invalidate();
            }
            if (seconds >= 2.000) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId6);
                invalidate();
            }
            if (seconds >= 2.100) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId7);
                invalidate();
            }
            if (seconds >= 2.200) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId8);
                invalidate();
            }
            if (seconds >= 2.300) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId9);
                invalidate();
            }
            if (seconds >= 2.400) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId10);
                invalidate();
            }
            if (seconds >= 2.500) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId11);
                invalidate();
            }
            if (seconds >= 2.600) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId12);
                invalidate();
            }
            if (seconds >= 2.700) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId13);
                invalidate();
            }
            if (seconds >= 2.800) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId14);
                invalidate();
            }
            if (seconds >= 2.900) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId15);
                invalidate();
            }
            if (seconds >= 3.000) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId16);
                invalidate();
            }
            if (seconds >= 3.100) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId17);
                invalidate();
            }
            if (seconds >= 3.200) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId18);
                invalidate();
            }
            if (seconds >= 3.300) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId19);
                invalidate();
            }
            if (seconds >= 3.400) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId20);
                invalidate();
            }
            if (seconds >= 3.500) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId21);
                invalidate();
            }
            if (seconds == 3.600) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId22);
                invalidate();
            }
            if (seconds >= 3.700) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId23);
                invalidate();
            }
            if (seconds >= 3.800) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId24);
                invalidate();
            }
            if (seconds >= 3.900) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId25);
                invalidate();
            }
            if (seconds >= 4.000) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId26);
                invalidate();
            }
            if (seconds >= 4.100) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId27);
                invalidate();
            }
            if (seconds >= 4.200) {
                mBackgroundBitmap =  BitmapFactory.decodeResource(getResources(), backgroundResId28);
                invalidate();
            }
            if (seconds >= 4.300) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId29);
                invalidate();
            }
            if (seconds >= 4.400) {
                mBackgroundBitmap =  BitmapFactory.decodeResource(getResources(), backgroundResId30);
                invalidate();
            }
            if (seconds >= 5.400) {
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId1);
                invalidate();
            }

            String text = mAmbient
                    ? String.format("%d:%02d", mCalendar.get(Calendar.HOUR_OF_DAY),  mCalendar.get(Calendar.MINUTE))
                    : String.format("%d:%02d:%02d", mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), mCalendar.get(Calendar.SECOND));
            canvas.drawText(text, mXOffset, mYOffset, mTextPaint);

            String dateText = String.format("%d/%02d/%02d", mCalendar.get(Calendar.DAY_OF_MONTH), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.YEAR));

            canvas.drawText(dateText, mXOffsetCal, mYOffsetCal, mTextPaintCal);
        }

        /**
         * Starts the {@link #mUpdateTimeHandler} timer if it should be running and isn't currently
         * or stops it if it shouldn't be running but currently is.
         */
        private void updateTimer() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (shouldTimerBeRunning()) {
                mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }

        /**
         * Returns whether the {@link #mUpdateTimeHandler} timer should be running. The timer should
         * only run when we're visible and in interactive mode.
         */
        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        /**
         * Handle updating the time periodically in interactive mode.
         */
        private void handleUpdateTimeMessage() {
            invalidate();
            if (shouldTimerBeRunning()) {
                long timeMs = System.currentTimeMillis();
                long delayMs = INTERACTIVE_UPDATE_RATE_MS
                        - (timeMs % INTERACTIVE_UPDATE_RATE_MS);
                mUpdateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
            }
        }
    }
}
