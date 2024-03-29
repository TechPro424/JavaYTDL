/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package in.techpro424;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestPlaylistInfo;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.playlist.PlaylistInfo;
import com.github.kiulian.downloader.model.playlist.PlaylistVideoDetails;
import com.github.kiulian.downloader.model.videos.formats.Format;

public class Library {
    static YoutubeDownloader downloader = new YoutubeDownloader();

    public static String downloadSingleVideo(String videoId, String renameTo) throws TimeoutException {

        Format format = DownloadMethods.getBestFormat(videoId);

        // async downloading without callback
        RequestVideoFileDownload request = new RequestVideoFileDownload(format).renameTo(renameTo).overwriteIfExists(true).async();
        Response<File> response = downloader.downloadVideoFile(request);
        File data = response.data(20, TimeUnit.SECONDS); // will block current thread and may throw TimeoutExeption
        return data.getAbsolutePath();
    }

    public static void downloadPlaylist(String playlistId) throws IOException {
        RequestPlaylistInfo request = new RequestPlaylistInfo(playlistId).async();
        Response<PlaylistInfo> response = downloader.getPlaylistInfo(request);
        PlaylistInfo playlistInfo = response.data();

        for (PlaylistVideoDetails playlistVideoDetails : playlistInfo.videos()) {
            try {
                downloadSingleVideo(playlistVideoDetails.videoId(), playlistVideoDetails.title());
            } catch (TimeoutException e) {
                System.out.println("Error downloading video\nID: " + playlistVideoDetails.videoId()+ "\nName: " + playlistVideoDetails.title());
                e.printStackTrace();
            }
        }

        new ZipUtils("videos").main(playlistId + ".zip");
        //ZipUtil.pack(new File("videos"), new File(playlistId + ".zip"));
        System.out.println("Zipped");


    }

    public static void main(String[] args) {
        try {
            //downloadSingleVideo("dIWNltBsq10");
            downloadPlaylist("PLpHMGWl4vYeh8ax79pjzO0ubhINgKeRL-");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
