package com.bravvura.nestledtime.userstory.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.customview.MyMediaPlayer;
import com.bravvura.nestledtime.customview.spinnerdatepicker.DatePicker;
import com.bravvura.nestledtime.customview.spinnerdatepicker.DatePickerDialog;
import com.bravvura.nestledtime.customview.spinnerdatepicker.SpinnerDatePickerDialogBuilder;
import com.bravvura.nestledtime.mediagallery.model.MEDIA_CELL_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.userstory.customview.UserStoryMediaView;
import com.bravvura.nestledtime.userstory.listener.OnMediaClickListener;
import com.bravvura.nestledtime.userstory.model.UserStoryAudioModel;
import com.bravvura.nestledtime.userstory.model.UserStoryDateModel;
import com.bravvura.nestledtime.userstory.model.UserStoryElement;
import com.bravvura.nestledtime.userstory.model.UserStoryElementType;
import com.bravvura.nestledtime.userstory.model.UserStoryMediaModel;
import com.bravvura.nestledtime.utils.CloudinaryManager;
import com.bravvura.nestledtime.utils.DatetUtils;
import com.bravvura.nestledtime.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 26-02-2018.
 */

public class UserStoryElementListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TITLE = 1, VIEW_TEXT = 2, VIEW_MEDIA = 3, VIEW_LOCATION = 4, VIEW_AUDIO = 5, VIEW_DATE = 6, VIEW_BLANK = 7;
    private MyMediaPlayer mediaPlayer;
    private UserStoryMediaView lastMediaPlayerView;
    OnMediaClickListener mediaClickListener;
    ArrayList<UserStoryElement> userStoryElements = new ArrayList<>();
    public int middlePosition;
    public boolean showMediaPlayer;

    public UserStoryElementListAdapter(Context context, ArrayList<UserStoryElement> userStoryElements, OnMediaClickListener mediaClickListener) {
        this.mediaClickListener = mediaClickListener;
        this.userStoryElements = userStoryElements;
        mediaPlayer = new MyMediaPlayer(context);

    }

    public void setResult(ArrayList<UserStoryElement> userStoryElements) {
        this.userStoryElements = userStoryElements;
    }

    public void appendResult(ArrayList<UserStoryElement> userStoryElements) {
        if (this.userStoryElements == null)
            this.userStoryElements = new ArrayList<>();
        this.userStoryElements.addAll(userStoryElements);
    }

    public void addResult(UserStoryElement userStoryElement) {
        if (this.userStoryElements == null)
            this.userStoryElements = new ArrayList<>();
        this.userStoryElements.add(userStoryElement);
    }

    public void removeIndex(int index) {
        if (!Utils.isEmpty(userStoryElements) && userStoryElements.size() > index) {
//            userStoryElements.get(index).isdeleted = true;
            userStoryElements.remove(index);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (userStoryElements.get(position).isdeleted)
            return VIEW_BLANK;
        switch (userStoryElements.get(position).elementType) {
            case ELEMENT_TYPE_AUDIO:
                return VIEW_AUDIO;
            case ELEMENT_TYPE_DATE:
                return VIEW_DATE;
            case ELEMENT_TYPE_LOCATION:
                return VIEW_LOCATION;
            case ELEMENT_TYPE_MEDIA:
                return VIEW_MEDIA;
            case ELEMENT_TYPE_TITLE:
                return VIEW_TITLE;
            case ELEMENT_TYPE_TEXT:
                return VIEW_TEXT;
            default:
                return VIEW_TEXT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_MEDIA) {
            return new MediaViewHolder(View.inflate(parent.getContext(), R.layout.cell_user_story_media, null));
        } else if (viewType == VIEW_DATE) {
            return new DateViewHolder(View.inflate(parent.getContext(), R.layout.cell_user_story_date, null));
        } else if (viewType == VIEW_AUDIO) {
            return new AudioViewHolder(View.inflate(parent.getContext(), R.layout.cell_user_story_audio, null));
        } else if (viewType == VIEW_LOCATION) {
            return new LocationViewHolder(View.inflate(parent.getContext(), R.layout.cell_user_story_location, null));
        } else if (viewType == VIEW_TEXT) {
            return new TextViewHolder(View.inflate(parent.getContext(), R.layout.cell_user_story_text, null));
        } else if (viewType == VIEW_TITLE) {
            return new TextViewHolder(View.inflate(parent.getContext(), R.layout.cell_user_story_title, null));
        } else {
            return new BlankViewHolder(new View(parent.getContext()));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MediaViewHolder) {
            ((MediaViewHolder) holder).populateMedia(userStoryElements.get(position).mediaModel);
        } else if (holder instanceof LocationViewHolder) {
            Glide.with(holder.itemView.getContext())
                    .load(Utils.getStaticMapUrl(userStoryElements.get(position).addressModel.latLng))
                    .into(((LocationViewHolder) holder).imageView);
        } else if (holder instanceof TextViewHolder) {
            if (userStoryElements.get(position).elementType == UserStoryElementType.ELEMENT_TYPE_TITLE) {
                ((TextViewHolder) holder).edit_text.setHint("Enter your memory's title here");
            } else if (userStoryElements.get(position).elementType == UserStoryElementType.ELEMENT_TYPE_TEXT) {
                ((TextViewHolder) holder).edit_text.setHint("Text");
            }
            ((TextViewHolder) holder).edit_text.setText(userStoryElements.get(position).textModel.data);
            if (userStoryElements.get(position).textModel.autoFocus) {
                userStoryElements.get(position).textModel.autoFocus = false;
                ((TextViewHolder) holder).edit_text.requestFocus();
            }
        } else if (holder instanceof AudioViewHolder) {
            ((AudioViewHolder) holder).reset();
            ((AudioViewHolder) holder).setUpDataSource(userStoryElements.get(position).audioModel);
        } else if (holder instanceof DateViewHolder) {
            ((DateViewHolder) holder).populateDate(userStoryElements.get(position).dateModel);
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof AudioViewHolder) {

            ((AudioViewHolder) holder).reset();
        }
    }

    @Override
    public int getItemCount() {
        return userStoryElements.size();
    }

    public ArrayList<UserStoryElement> getAllItems() {
        return userStoryElements;
    }

    public void updateHighlightPosition(int highlightPosition) {
        middlePosition = highlightPosition;
    }

    public void checkStopForMedia() {
        if (lastMediaPlayerView != null && lastMediaPlayerView.getPosition() != middlePosition) {
            lastMediaPlayerView.releaseMediaPlayer(middlePosition);
        }
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaClickListener != null)
                        mediaClickListener.onClick(BaseViewHolder.this, userStoryElements.get(getAdapterPosition()), getAdapterPosition());
                }
            });
            if (itemView.findViewById(R.id.ic_close) != null)
                itemView.findViewById(R.id.ic_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRemoveClick();
                    }
                });
        }

        protected void onRemoveClick() {
            if (mediaClickListener != null)
                mediaClickListener.onRemoveClick(userStoryElements.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    class DateViewHolder extends BaseViewHolder {

        private final TextView textDate;
        private UserStoryDateModel dateModel;

        public DateViewHolder(final View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.text_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar todayCalendar = Calendar.getInstance();
                    final Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateModel.date);
                    SpinnerDatePickerDialogBuilder dialog = new SpinnerDatePickerDialogBuilder()
                            .context(itemView.getContext())
                            .callback(new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    calendar.set(year, monthOfYear, dayOfMonth);
                                    dateModel.date = calendar.getTime();
                                    populateDate(dateModel);
                                }
                            })
//                            .spinnerTheme(spinnerTheme)
                            .defaultDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                            .maxDate(todayCalendar.get(Calendar.YEAR), todayCalendar.get(Calendar.MONTH), todayCalendar.get(Calendar.DAY_OF_MONTH));
                    dialog.build().show();
                }
            });

        }

        public void populateDate(UserStoryDateModel dateModel) {
            if (dateModel != null && dateModel.date != null) {
                textDate.setText(DatetUtils.getDateElementString(dateModel.date));
                this.dateModel = dateModel;
            }
        }
    }

    class BlankViewHolder extends BaseViewHolder {

        public BlankViewHolder(View itemView) {
            super(itemView);
        }
    }

    class AudioViewHolder extends BaseViewHolder implements View.OnClickListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnBufferingUpdateListener {
        private ImageView imagePlayPause;
        private SeekBar seekBar;
        private TextView textDuration;
        private MediaPlayer audioPlayer;
        private ProgressBar progressBar;
        private UserStoryAudioModel audioModel;
        private boolean isPlaying;
        private Handler handler = new Handler();
        private Runnable updateTimeTask = new Runnable() {
            @Override
            public void run() {
                try {
                    if (isMediaPlaying()) {
                        seekBar.setProgress(audioPlayer.getCurrentPosition());
                        seekBar.setMax(audioPlayer.getDuration());
                        int current = audioPlayer.getCurrentPosition() / 1000;
                        textDuration.setText(String.format("%02d", current / 3600) + ":" + String.format("%02d", current / 60) + ":" + String.format("%02d", current % 60));
                    }
                    updateProgressBar();
                } catch (Exception x) {
                }
            }
        };
        private ImageView imageView;


        public boolean isMediaPlaying() {
            try {
                return audioPlayer != null && audioPlayer.isPlaying();
            } catch (Exception x) {
                return false;
            }
        }

        public AudioViewHolder(View itemView) {
            super(itemView);
            initComponent();
        }

        private void initComponent() {
            imagePlayPause = itemView.findViewById(R.id.image_play_pause);
            seekBar = itemView.findViewById(R.id.seek_bar);
            imageView = itemView.findViewById(R.id.image_view);
            seekBar.setOnSeekBarChangeListener(this);
            textDuration = itemView.findViewById(R.id.text_duration);
            progressBar = itemView.findViewById(R.id.progress_bar);
            imagePlayPause.setOnClickListener(this);
        }

        public void setUpDataSource(UserStoryAudioModel audioModel) {
            this.audioModel = audioModel;
            Glide.with(itemView.getContext()).load(CloudinaryManager.getAudioWaveUrl(audioModel.publicId)).into(imageView);
        }

        public void play() {
            audioPlayer = new MediaPlayer();
            try {
                audioPlayer.setDataSource(audioModel.audioUrl);
                audioPlayer.prepare();
                audioPlayer.start();
                seekBar.setEnabled(false);
                showProgressBar();
                audioPlayer.setOnCompletionListener(this);
                audioPlayer.setOnPreparedListener(this);
                audioPlayer.setOnSeekCompleteListener(this);
                audioPlayer.setOnBufferingUpdateListener(this);
                isPlaying = true;
            } catch (Exception e) {
                // make something
            }
        }

        public void pause() {
            if (audioPlayer != null && audioPlayer.isPlaying()) {
                audioPlayer.pause();
            }
            imagePlayPause.setImageResource(R.drawable.ic_play_arrow);
            isPlaying = false;
        }

        public void resume() {
            if (audioPlayer != null && !audioPlayer.isPlaying()) {
                audioPlayer.start();
            }
            imagePlayPause.setImageResource(R.drawable.ic_pause_arrow);
            isPlaying = true;
        }

        public void reset() {
            imagePlayPause.setImageResource(R.drawable.ic_play_arrow);
            if (audioPlayer != null)
                audioPlayer.stop();
            hideProgressBar();
            seekBar.setProgress(0);
            seekBar.setEnabled(false);
            textDuration.setText("00:00:00");
            audioPlayer = null;
            isPlaying = false;
            handler.removeCallbacks(updateTimeTask);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.image_play_pause) {
                if (audioPlayer == null) {
                    play();
                } else if (isPlaying) {
                    pause();
                } else {
                    resume();
                }
            }
        }

        void showProgressBar() {
            imagePlayPause.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        void hideProgressBar() {
            imagePlayPause.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            reset();
        }

        private void updateProgressBar() {
            handler.postDelayed(updateTimeTask, 200);
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            updateProgressBar();
            seekBar.setEnabled(true);
            hideProgressBar();
            imagePlayPause.setImageResource(R.drawable.ic_pause_arrow);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                audioPlayer.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onSeekComplete(MediaPlayer mp) {
            hideProgressBar();
        }

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            int total = seekBar.getMax();
            int secondary = total * percent / 100;
            seekBar.setSecondaryProgress(secondary);
        }
    }

    class TextViewHolder extends BaseViewHolder {

        public final EditText edit_text;

        public TextViewHolder(View itemView) {
            super(itemView);
            edit_text = itemView.findViewById(R.id.edit_text);
            edit_text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    userStoryElements.get(getAdapterPosition()).textModel.data = s.toString();
                }
            });

        }

        @Override
        protected void onRemoveClick() {
            if (userStoryElements.get(getAdapterPosition()).elementType == UserStoryElementType.ELEMENT_TYPE_TEXT) {
                super.onRemoveClick();
            } else if (userStoryElements.get(getAdapterPosition()).elementType == UserStoryElementType.ELEMENT_TYPE_TITLE) {
                edit_text.setText("");
            }
        }
    }

    class LocationViewHolder extends BaseViewHolder {

        private final ImageView imageView;

        public LocationViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }

    public class MediaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final UserStoryMediaView mediaView1, mediaView2, mediaView3, mediaView4;
        public final TextView textPicMore, textTitle;
        private final LinearLayout layoutMorePic;
        private final View textEdit;
        private final ProgressBar progressBar;
        private final View imageProgress;
        public View layouotImage4;
        private UserStoryMediaModel userStoryMediaModel;
        private boolean isUploaded;


        public MediaViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_title);
            mediaView1 = itemView.findViewById(R.id.media_view_1);
            layoutMorePic = itemView.findViewById(R.id.layout_more_pic);
            mediaView2 = itemView.findViewById(R.id.media_view_2);
            mediaView3 = itemView.findViewById(R.id.media_view_3);
            mediaView4 = itemView.findViewById(R.id.media_view_4);
            textPicMore = itemView.findViewById(R.id.text_more_pic);
            layouotImage4 = itemView.findViewById(R.id.layout_image_4);
            (textEdit = itemView.findViewById(R.id.text_edit)).setOnClickListener(this);
            progressBar = itemView.findViewById(R.id.progress_bar);
            imageProgress = itemView.findViewById(R.id.image_progress);
            mediaView1.setOnClickListener(this);
            mediaView2.setOnClickListener(this);
            mediaView3.setOnClickListener(this);
            mediaView4.setOnClickListener(this);

        }

        void showMediaGallery(final int index) {
            if (mediaClickListener != null)
                mediaClickListener.onClick(MediaViewHolder.this, userStoryElements.get(getAdapterPosition()), index);
        }

        public void populateMedia(UserStoryMediaModel userStoryMediaModel) {
            this.userStoryMediaModel = userStoryMediaModel;
            textTitle.setText(userStoryMediaModel.title);
            isUploaded = updateProgress();
            if (isUploaded) {
                progressBar.setVisibility(View.GONE);
                imageProgress.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.VISIBLE);
                imageProgress.setVisibility(View.VISIBLE);
            }
            showMediaImage();
            if (showMediaPlayer && middlePosition == getAdapterPosition()) {
                UserStoryMediaView userStoryMediaView = getVideoView();
                if (userStoryMediaView != null) {
                    if (UserStoryElementListAdapter.this.lastMediaPlayerView != null) {
                        UserStoryElementListAdapter.this.lastMediaPlayerView.releaseMediaPlayer(getAdapterPosition());
                    }
                    UserStoryElementListAdapter.this.lastMediaPlayerView = userStoryMediaView;
                    lastMediaPlayerView.playMediaPlayer(mediaPlayer, getAdapterPosition());
                }
            } else {
                if (lastMediaPlayerView != null && lastMediaPlayerView.getPosition() != middlePosition)
                    lastMediaPlayerView.releaseMediaPlayer(getAdapterPosition());
            }
        }

        private boolean updateProgress() {
            int totalProgress, progress;
            totalProgress = userStoryMediaModel.mediaCount + 1;
            progress = 1;
            for (MediaModel mediaModel : userStoryMediaModel.mediaModels) {
                if (mediaModel.isUploaded())
                    progress++;
            }
            progressBar.setMax(totalProgress);
            progressBar.setProgress(progress);
            return totalProgress == progress;
        }

        private void showMediaImage() {
            ArrayList<MediaModel> mediaModels = userStoryMediaModel.mediaModels;
            if (mediaModels.size() == 1) {
                mediaView1.showMedia(mediaModels.get(0));
                layoutMorePic.setVisibility(View.GONE);
                mediaView2.hideView();
                mediaView3.hideView();
                mediaView4.hideView();
                layouotImage4.setVisibility(View.GONE);
                textPicMore.setVisibility(View.GONE);
            } else {
                layoutMorePic.setVisibility(View.VISIBLE);
                if (mediaModels.size() == 2) {
                    mediaView1.showMedia(mediaModels.get(0));
                    mediaView2.showMedia(mediaModels.get(1));
                    mediaView3.hideView();
                    mediaView4.hideView();
                    layouotImage4.setVisibility(View.GONE);
                    textPicMore.setVisibility(View.GONE);
                } else if (mediaModels.size() == 3) {
                    mediaView1.showMedia(mediaModels.get(0));
                    mediaView2.showMedia(mediaModels.get(1));
                    mediaView3.showMedia(mediaModels.get(2));
                    mediaView4.hideView();
                    layouotImage4.setVisibility(View.GONE);
                    textPicMore.setVisibility(View.GONE);
                } else if (mediaModels.size() == 4) {
                    mediaView1.showMedia(mediaModels.get(0));
                    mediaView2.showMedia(mediaModels.get(1));
                    mediaView3.showMedia(mediaModels.get(2));
                    mediaView4.showMedia(mediaModels.get(3));
                    layouotImage4.setVisibility(View.VISIBLE);
                    textPicMore.setVisibility(View.GONE);
                } else if (mediaModels.size() > 4) {
                    mediaView1.showMedia(mediaModels.get(0));
                    mediaView2.showMedia(mediaModels.get(1));
                    mediaView3.showMedia(mediaModels.get(2));
                    mediaView4.showMedia(mediaModels.get(3));
                    layouotImage4.setVisibility(View.VISIBLE);
                    textPicMore.setVisibility(View.VISIBLE);
                    textPicMore.setText(mediaModels.size() - 4 + "+");
                }
            }
        }

        public UserStoryMediaView getVideoView() {
            if (mediaView1.mediaModel != null && mediaView1.mediaModel.mediaCellType == MEDIA_CELL_TYPE.TYPE_VIDEO)
                return mediaView1;
            else if (mediaView2.mediaModel != null && mediaView2.mediaModel.mediaCellType == MEDIA_CELL_TYPE.TYPE_VIDEO)
                return mediaView2;
            else if (mediaView3.mediaModel != null && mediaView3.mediaModel.mediaCellType == MEDIA_CELL_TYPE.TYPE_VIDEO)
                return mediaView3;
            else if (mediaView4.mediaModel != null && mediaView4.mediaModel.mediaCellType == MEDIA_CELL_TYPE.TYPE_VIDEO)
                return mediaView4;
            else return null;
        }

        @Override
        public void onClick(View v) {
            if (v.equals(mediaView1))
                showMediaGallery(0);
            else if (v.equals(mediaView2))
                showMediaGallery(1);
            else if (v.equals(mediaView3))
                showMediaGallery(2);
            else if (v.equals(mediaView4))
                showMediaGallery(3);
            else if (v.equals(textEdit)) {
                mediaClickListener.onEditClick(userStoryElements.get(getAdapterPosition()), getAdapterPosition());
            }
        }
    }
}
