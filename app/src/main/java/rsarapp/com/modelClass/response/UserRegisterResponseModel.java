package rsarapp.com.modelClass.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRegisterResponseModel {

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("User_Type")
    @Expose
    private String userType;
    @SerializedName("Class_Id")
    @Expose
    private String classId;
    @SerializedName("Access")
    @Expose
    private String access;
    @SerializedName("Action")
    @Expose
    private String action;
    @SerializedName("OTP")
    @Expose
    private String otp;
    @SerializedName("OTP_Popup")
    @Expose
    private String oTPPopup;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("School_Name")
    @Expose
    private String schoolName;
    @SerializedName("School_Folder_Name")
    @Expose
    private String schoolFolderName;
    @SerializedName("Bg_Code")
    @Expose
    private String bgCode;
    @SerializedName("Top_BgCode")
    @Expose
    private String topBgCode;
    @SerializedName("Button_Bg_Color")
    @Expose
    private String buttonBgColor;
    @SerializedName("School_Name_Color")
    @Expose
    private String schoolNameColor;
    @SerializedName("Folder_Name")
    @Expose
    private String folderName;
    @SerializedName("School_Folder_Path")
    @Expose
    private String schoolFolderPath;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Activation_Status")
    @Expose
    private String activationStatus;
    @SerializedName("Registered_UserId")
    @Expose
    private String registeredUserId;
    @SerializedName("Device_Id")
    @Expose
    private String deviceId;
    @SerializedName("Restrict_SD")
    @Expose
    private String restrictSD;
    @SerializedName("School_UI")
    @Expose
    private String schoolUI;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getOTPPopup() {
        return oTPPopup;
    }

    public void setOTPPopup(String oTPPopup) {
        this.oTPPopup = oTPPopup;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolFolderName() {
        return schoolFolderName;
    }

    public void setSchoolFolderName(String schoolFolderName) {
        this.schoolFolderName = schoolFolderName;
    }

    public String getBgCode() {
        return bgCode;
    }

    public void setBgCode(String bgCode) {
        this.bgCode = bgCode;
    }

    public String getTopBgCode() {
        return topBgCode;
    }

    public void setTopBgCode(String topBgCode) {
        this.topBgCode = topBgCode;
    }

    public String getButtonBgColor() {
        return buttonBgColor;
    }

    public void setButtonBgColor(String buttonBgColor) {
        this.buttonBgColor = buttonBgColor;
    }

    public String getSchoolNameColor() {
        return schoolNameColor;
    }

    public void setSchoolNameColor(String schoolNameColor) {
        this.schoolNameColor = schoolNameColor;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getSchoolFolderPath() {
        return schoolFolderPath;
    }

    public void setSchoolFolderPath(String schoolFolderPath) {
        this.schoolFolderPath = schoolFolderPath;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getActivationStatus() {
        return activationStatus;
    }

    public void setActivationStatus(String activationStatus) {
        this.activationStatus = activationStatus;
    }

    public String getRegisteredUserId() {
        return registeredUserId;
    }

    public void setRegisteredUserId(String registeredUserId) {
        this.registeredUserId = registeredUserId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getRestrictSD() {
        return restrictSD;
    }

    public void setRestrictSD(String restrictSD) {
        this.restrictSD = restrictSD;
    }

    public String getSchoolUI() {
        return schoolUI;
    }

    public void setSchoolUI(String schoolUI) {
        this.schoolUI = schoolUI;
    }

}