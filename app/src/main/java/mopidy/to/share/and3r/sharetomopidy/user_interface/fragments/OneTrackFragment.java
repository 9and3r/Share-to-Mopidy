package mopidy.to.share.and3r.sharetomopidy.user_interface.fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import mopidy.to.share.and3r.sharetomopidy.mopidy.MopidyStatus;
import mopidy.to.share.and3r.sharetomopidy.R;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.MopidyTlTrack;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.OnImageAndPaletteReady;
import mopidy.to.share.and3r.sharetomopidy.mopidy.data.TaskImage;
import mopidy.to.share.and3r.sharetomopidy.utils.PaletteManager;


public class OneTrackFragment extends Fragment implements OnImageAndPaletteReady {


    private ViewGroup rootView;
    private MopidyTlTrack track;
    private TextView trackName;
    private TextView albumName;
    private TextView artistsNames;
    private ImageView albumImageView;

    private TaskImage imageTask;




    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.one_track_view, container, false);
        setRetainInstance(false);
        trackName = (TextView) rootView.findViewById(R.id.trackNametextView);
        albumName = (TextView) rootView.findViewById(R.id.albumNametextView);
        artistsNames = (TextView) rootView.findViewById(R.id.artisttextView);
        albumImageView = (ImageView) rootView.findViewById(R.id.albumImageView);
        track = MopidyStatus.get().getTrack(getArguments().getInt("pos"));
        if (track != null){
            init();
            albumImageView.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
        }
        return rootView;
    }

    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        public void onGlobalLayout() {
            albumImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            imageTask = new TaskImage(OneTrackFragment.this, track.getAlbum(), albumImageView.getWidth(), albumImageView.getHeight());
            imageTask.execute(getActivity().getApplicationContext());
        }
    };



    private void init(){
        trackName.setText(track.getTrackString());
        albumName.setText(track.getAlbumString());
        artistsNames.setText(track.getArtistsString());
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (imageTask != null && !imageTask.isCancelled()){
            imageTask.cancel(true);
        }

    }

    @Override
    public void onImageAndPaletteReady(Bitmap bitmap, Palette palette) {
        if (bitmap != null){
            albumImageView.setImageBitmap(bitmap);
        }

        if (palette != null){
            Palette.Swatch s = PaletteManager.getVibrantSwatch(palette);
            if (s != null){
                rootView.setBackgroundColor(s.getRgb());
                trackName.setTextColor(s.getTitleTextColor());
                albumName.setTextColor(s.getTitleTextColor());
                artistsNames.setTextColor(s.getTitleTextColor());
            }
        }
    }



    public MopidyTlTrack getTrack() {
        return track;
    }
}
