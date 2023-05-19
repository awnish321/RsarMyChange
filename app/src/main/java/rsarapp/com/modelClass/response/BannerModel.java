package rsarapp.com.modelClass.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BannerModel {

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Banner_Data")
    @Expose
    private List<BannerDatum> bannerData;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BannerDatum> getBannerData() {
        return bannerData;
    }

    public void setBannerData(List<BannerDatum> bannerData) {
        this.bannerData = bannerData;
    }

    public static class BannerDatum {

        @SerializedName("Banner_Id")
        @Expose
        private String bannerId;
        @SerializedName("Banner_URL")
        @Expose
        private String bannerURL;

        public String getBannerId() {
            return bannerId;
        }

        public void setBannerId(String bannerId) {
            this.bannerId = bannerId;
        }

        public String getBannerURL() {
            return bannerURL;
        }

        public void setBannerURL(String bannerURL) {
            this.bannerURL = bannerURL;
        }

    }

}