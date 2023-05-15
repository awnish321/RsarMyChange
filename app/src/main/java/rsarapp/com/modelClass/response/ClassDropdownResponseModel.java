package rsarapp.com.modelClass.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClassDropdownResponseModel {

    @SerializedName("Status")
    @Expose
    private String status;
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

    }


}



