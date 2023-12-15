package com.example.gestiondecourrier.ui.ui.viewmodel.api;

import com.example.gestiondecourrier.ui.pojo.ArchivePk;
import com.example.gestiondecourrier.ui.pojo.Attendance;
import com.example.gestiondecourrier.ui.pojo.Category;
import com.example.gestiondecourrier.ui.pojo.CategoryInfo;
import com.example.gestiondecourrier.ui.pojo.CategoryResponse;
import com.example.gestiondecourrier.ui.pojo.CommentResponse;
import com.example.gestiondecourrier.ui.pojo.Contact;
import com.example.gestiondecourrier.ui.pojo.Email;
import com.example.gestiondecourrier.ui.pojo.Employee;
import com.example.gestiondecourrier.ui.pojo.EmployeeResponse;
import com.example.gestiondecourrier.ui.pojo.FavoritePk;
import com.example.gestiondecourrier.ui.pojo.GmailResponse;
import com.example.gestiondecourrier.ui.pojo.JwtToken;
import com.example.gestiondecourrier.ui.pojo.MailResponse;
import com.example.gestiondecourrier.ui.pojo.AuthRequest;
import com.example.gestiondecourrier.ui.pojo.ReceivedMail;
import com.example.gestiondecourrier.ui.pojo.Report;
import com.example.gestiondecourrier.ui.pojo.Structure;
import com.example.gestiondecourrier.ui.pojo.TraceResponse;
import com.example.gestiondecourrier.ui.pojo.TraitPk;
import com.example.gestiondecourrier.ui.pojo.User;
import com.example.gestiondecourrier.ui.pojo.UserResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface Api {

    //Contact
    @GET("api/v1/contact/read")
    Observable<List<Contact>> getContacts();
    @POST("api/v1/contact/create")
    Completable addContact(@Body Contact contact);
    @DELETE("api/v1/contact/delete/{id}")
    Completable deleteContact(@Path("id") long id);
    @PUT("api/v1/contact/update/{id}")
    Completable updateContact(@Path("id") long id,@Body Contact contact);

    //Trace
    @GET("api/v1/trace/read")
    Observable<List<TraceResponse>> getTraces();



    //Mail
    @Multipart
    @POST("api/v1/mail/upload")
    Completable createMail(@Part List<MultipartBody.Part> file, @Part("description") RequestBody requestBody);
    @GET("api/v1/mail/all/{id}/{userId}/{reference}")
    Observable<List<MailResponse>> allMails(@Path("id") long id,@Path("userId") long userId,@Path("reference") String reference);
    @GET("api/v1/mail/send/{id}/{userId}")
    Observable<List<MailResponse>> sendMails(@Path("id") long id,@Path("userId") long userId);
    @GET("api/v1/mail/received/{reference}/{userId}")
    Observable<List<MailResponse>> receivedMails(@Path("reference") String  reference,@Path("userId") long userId);
    @GET("api/v1/mail/trait/{reference}/{userId}")
    Observable<List<MailResponse>> traitMails(@Path("reference") String reference,@Path("userId") long userId);
    @GET("api/v1/mail/toTrait/{reference}/{userId}")
    Observable<List<MailResponse>> toTraitMails(@Path("reference") String reference,@Path("userId") long userId);
    @GET("api/v1/mail/favorite/{id}/{userId}")
    Observable<List<MailResponse>> favoriteMails(@Path("id") long id,@Path("userId") long userId);
    @GET("api/v1/mail/archive/{id}/{userId}/{reference}")
    Observable<List<MailResponse>> archiveMails(@Path("id") long id,@Path("userId") long userId,@Path("reference") String reference);
    @GET("api/v1/mail/reference/{id}")
    Observable<List<String>> references(@Path("id") long id);
    @Multipart
    @PUT("api/v1/mail/update/{id}")
    Completable updateMail(@Path("id") long id,@Part List<MultipartBody.Part> file,@Part("description") RequestBody requestBody );
    @PUT("api/v1/mail/update/received/{id}")
    Completable updateReceivedMail(@Path("id") long id,@Body ReceivedMail mail);




    //Gmail
    @Multipart
    @POST("api/v1/gmail/upload")
    Completable uploadPdfFromGmail(@Part List<MultipartBody.Part> file,@Part("description") RequestBody requestBody);
    @GET("api/v1/gmail/getAll/{id}")
    Observable<List<GmailResponse>> getGmail(@Path("id") long id);
    @GET("api/v1/gmail/download/email")
    Observable<List<String>> getEmails();



    //User
    @Multipart
    @PUT("api/v1/user/update/photo/{id}")
    Completable updateUserPhoto(@Path("id") long id,@Part MultipartBody.Part file);
    @GET("api/v1/user/getUsers")
    Observable<List<UserResponse>> getUsers();
    @GET("api/v1/user/get/{id}")
    Observable<UserResponse> getUserById(@Path("id") long id);
    @POST("api/v1/user/create")
    Completable createUser(@Body User user);
    @PUT("api/v1/user/update/password/{id}")
    Completable updatePassword(@Path("id") long id, @Body User user);
    @PUT("api/v1/user/generate/password/{id}")
    Completable generatePassword(@Path("id") long id);
    @PUT("api/v1/user/update/token/{id}")
    Completable updateToken(@Path("id") long id,@Body String token);
    @PUT("api/v1/user/update/notification/{id}")
    Completable updateNotification(@Path("id") long id);
    @PUT("api/v1/user/update/info/{id}")
    Completable updateUserInfo(@Path("id") long id,@Body User user);


    //Structure
    @GET("api/v1/structure/getStructures")
    Observable<List<Structure>> getStructures();
    @GET("api/v1/structure/designation")
    Observable<List<String>> getStructureDesignation();
    @GET("api/v1/structure/getStructureById/{id}")
    Observable<Structure> getStructure(@Path("id") long id);
    @POST("api/v1/structure/create")
    Completable addStructure(@Body Structure structure);
    @PUT("api/v1/structure/update/{id}")
    Completable updateStructure(@Path("id") long id,@Body Structure structure);


    //Authentication
    @POST("authentication/auth")
    Observable<JwtToken> authenticate(@Body AuthRequest signIn);
    //logout
    @POST("api/v1/authentication/logout")
    Observable<Response<Void>> logout();


    //Category
    @GET("api/v1/category/read")
    Observable<List<CategoryResponse>> getCategories();
    @GET("api/v1/category/read/info/Arriver/{id}")
    Observable<List<CategoryInfo>> getArrivedCategoryInfo(@Path("id") long id);
    @GET("api/v1/category/read/info/Depart/{id}")
    Observable<List<CategoryInfo>> getSendCategoryInfo(@Path("id") long id);
    @GET("api/v1/category/read/designation/{id}")
    Observable<List<String>> getCategoryDesignation(@Path("id") long id);
    @GET("api/v1/category/read/designation/arrived/{id}")
    Observable<List<String>> getArrivedCategoryDesignation(@Path("id") long id);
    @POST("api/v1/category/create")
    Completable addCategory(@Body Category category);
    @PUT("api/v1/category/update/{id}")
    Completable updateCategory(@Path("id") long id,@Body Category category);



    //Comment
    @GET("api/v1/comment/getComments/{pdf_id}")
    Observable<List<CommentResponse>> getComments(@Path("pdf_id") long id);



    //Employee
    @GET("api/v1/employee/read/{id}")
    Observable<List<EmployeeResponse>> getEmployees(@Path("id") long id);
    @POST("api/v1/employee/create")
    Completable addEmployee(@Body Employee employee);
    @PUT("api/v1/employee/update/{id}")
    Completable updateEmployee(@Path("id") long id,@Body Employee employee);
    @PUT("api/v1/employee/update/recuperation/{id}")
    Completable updateRecuperation(@Path("id") long id,@Body int recuperation);



    //Attendance
    @POST("api/v1/attendance/insert")
    Completable addAttendance(@Body Attendance attendance);



    //Report
    @GET("api/v1/report/{id}")
    Observable<List<Report>> getReportsByStructure(@Path("id") long id);
    @GET("api/v1/report/daily/{id}")
    Observable<Report> getDailyReport(@Path("id") long id);
    @GET("api/v1/report/all/{id}")
    Observable<List<Report>> getReports(@Path("id") long id);
    @PUT("api/v1/report/update/{id}")
    Completable updateApproved(@Path("id") long id);



    //Email
    @POST("api/v1/email/send")
    Completable sendEmail(@Body Email email);


    //Favorite
    @POST("api/v1/favorite/insert")
    Completable insertFavorite(@Body FavoritePk favorite);
    @PUT("api/v1/favorite/update")
    Completable updateFavorite(@Body FavoritePk favorite);


    //Archive
    @POST("api/v1/archive/insert")
    Completable insertArchive(@Body ArchivePk archive);
    @PUT("api/v1/archive/update")
    Completable updateArchive(@Body ArchivePk archive);


    //Trait
    @POST("api/v1/trait/insert")
    Completable insertTrait(@Body TraitPk trait);

}
