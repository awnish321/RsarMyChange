package rsarapp.com.modelClass.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllClassResponseModel {

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Restrict_SD")
    @Expose
    private String restrictSD;
    @SerializedName("School_UI")
    @Expose
    private String schoolUI;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("ClassData")
    @Expose
    private List<ClassDatum> classData;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ClassDatum> getClassData() {
        return classData;
    }

    public void setClassData(List<ClassDatum> classData) {
        this.classData = classData;
    }

    public static class ClassDatum {

        @SerializedName("Class_Id")
        @Expose
        private String classId;
        @SerializedName("Class_Name")
        @Expose
        private String className;
        @SerializedName("DB_Name")
        @Expose
        private String dBName;

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getDBName() {
            return dBName;
        }

        public void setDBName(String dBName) {
            this.dBName = dBName;
        }

    }

}


