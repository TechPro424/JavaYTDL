package in.techpro424;

import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.AudioFormat;
import com.github.kiulian.downloader.model.videos.formats.Format;
import com.github.kiulian.downloader.model.videos.formats.VideoFormat;

public class DownloadMethods {

    public static Format getBestFormat(String videoId) {
        RequestVideoInfo request = new RequestVideoInfo(videoId).async();
        Response<VideoInfo> response = Library.downloader.getVideoInfo(request);
        VideoInfo video = response.data();
        AudioFormat audioFormat = video.bestAudioFormat();
        VideoFormat videoFormat = video.bestVideoWithAudioFormat();

        Format bestFormat = (videoFormat.itag().audioQuality()
        .compareTo(audioFormat.itag().audioQuality()) > 0) ? videoFormat : audioFormat;
    
        return bestFormat;
    }
}
