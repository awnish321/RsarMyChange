package rsarapp.com.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rsarapp.com.modelClass.response.ClassDropdownResponseModel;
import rsarapp.com.modelClass.response.TestResponseModel;
import rsarapp.com.modelClass.response.UserRegisterResponseModel;
import rsarapp.com.modelClass.response.UserStatusResponseModel;

public interface MyAPIs

{
    @POST("rsarapp.services/activation")
    Call<UserRegisterResponseModel> registerUser(@Query("activation_code") String activation_code,
                                                 @Query("mid") String mid, @Query("mproduct") String mproduct, @Query("mbrand") String mbrand,
                                                 @Query("mmanufacture") String mmanufacture, @Query("mmodel") String mmodel, @Query("mdid") String mdid,
                                                 @Query("Restrict_SD") String Restrict_SD, @Query("package") String pack, @Query("version_name") String version_name,
                                                 @Query("version_code") String version_code, @Query("userType") String userType, @Query("classId") String classId,
                                                 @Query("action") String action, @Query("name") String name, @Query("mobile") String mobile, @Query("email") String email);
    @POST("rsarapp.services/activation")
    Call<UserRegisterResponseModel> newuserData(@Query("activation_code") String activation_code,
                                                 @Query("mid") String mid, @Query("mproduct") String mproduct, @Query("mbrand") String mbrand,
                                                 @Query("mmanufacture") String mmanufacture, @Query("mmodel") String mmodel, @Query("mdid") String mdid,
                                                 @Query("Restrict_SD") String Restrict_SD, @Query("package") String pack, @Query("version_name") String version_name,
                                                 @Query("version_code") String version_code, @Query("userType") String userType, @Query("classId") String classId,
                                                 @Query("action") String action, @Query("name") String name, @Query("mobile") String mobile, @Query("email") String email);

    @POST("rsarapp.services/checkActivation.php")
    Call<UserStatusResponseModel> getUserStatus(@Query("School_UI") String School_UI,
                                                @Query("Device_Id") String Device_Id, @Query("Restrict_SD") String Restrict_SD, @Query("package") String pack,
                                                @Query("version_name") String version_name, @Query("version_code") String version_code);

    @POST("rsarapp.services/class")
    Call<Class> getAllClass(@Query("classId") String classId,
                                                  @Query("userType") String userType, @Query("School_UI") String School_UI, @Query("action") String action,
                                                  @Query("Restrict_SD") String Restrict_SD);

    @POST("classDropdown.php")
    Call<ClassDropdownResponseModel> getAllClassDropdownList(@Query("action") String action);

    @POST("rsarapp.services/classDropdown.php")
    Call<ClassDropdownResponseModel> test(@Query("action") String action);

    @GET("news")
    Call<TestResponseModel> test();
}
