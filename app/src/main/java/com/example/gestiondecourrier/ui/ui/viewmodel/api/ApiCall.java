package com.example.gestiondecourrier.ui.ui.viewmodel.api;


import static com.example.gestiondecourrier.ui.ui.AuthenticationActivity.sp;

import com.example.gestiondecourrier.BuildConfig;
import com.example.gestiondecourrier.ui.deserializer.AuthenticationDeserializer;
import com.example.gestiondecourrier.ui.deserializer.CommentDeserializer;
import com.example.gestiondecourrier.ui.deserializer.GmailDeserializer;
import com.example.gestiondecourrier.ui.deserializer.MailDeserializer;
import com.example.gestiondecourrier.ui.deserializer.ReportDeserializer;
import com.example.gestiondecourrier.ui.deserializer.TraceDeserializer;
import com.example.gestiondecourrier.ui.deserializer.UserDeserializer;
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
import com.example.gestiondecourrier.ui.pojo.ReceivedMail;
import com.example.gestiondecourrier.ui.pojo.Report;
import com.example.gestiondecourrier.ui.pojo.Structure;
import com.example.gestiondecourrier.ui.pojo.TraceResponse;
import com.example.gestiondecourrier.ui.pojo.TraitPk;
import com.example.gestiondecourrier.ui.pojo.User;
import com.example.gestiondecourrier.ui.pojo.UserResponse;
import com.example.gestiondecourrier.ui.ui.viewmodel.MailViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCall {
    private static volatile ApiCall INSTANCE=null;
    Api api;

    public ApiCall(){
        Gson gson = new GsonBuilder()
                 .registerTypeAdapter(MailResponse.class, new MailDeserializer())
                .registerTypeAdapter(GmailResponse.class,new GmailDeserializer())
                .registerTypeAdapter(UserResponse.class,new UserDeserializer())
                .registerTypeAdapter(JwtToken.class,new AuthenticationDeserializer())
                .registerTypeAdapter(CommentResponse.class,new CommentDeserializer())
                .registerTypeAdapter(TraceResponse.class,new TraceDeserializer())
                .registerTypeAdapter(Report.class, new ReportDeserializer())
                .create();


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        // Add the AuthInterceptor to OkHttpClient
        httpClient.addInterceptor(new ApiInterceptor(sp.getString(BuildConfig.TOKEN,null)));
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build();


        api = retrofit.create(Api.class);
    }

    public static ApiCall getInstance(){
        if (INSTANCE==null){
            synchronized (MailViewModel.class){
                if (INSTANCE==null){
                    INSTANCE=new ApiCall();
                }
            }
        }
        return INSTANCE;
    }

    //contact
    public Observable<List<Contact>> getContacts(){
        return api.getContacts();
    }
    public Completable addContact(Contact contact){return  api.addContact(contact);}
    public Completable deleteContact(long id){return api.deleteContact(id);}
    public Completable updateContact(long id,Contact contact){return api.updateContact(id,contact);}

    //Gmail
    public Observable<List<GmailResponse>> getGmail(long id){
        return api.getGmail(id);
    }
    public Completable uploadPdfFromGmail(List<MultipartBody.Part> file,RequestBody description){return api.uploadPdfFromGmail(file,description);}
    public Observable<List<String>> getEmails(){
        return api.getEmails();
    }

    //Mail
    public Completable createMail(List<MultipartBody.Part> file, RequestBody description){return api.createMail(file,description);}
    public Observable<List<MailResponse>> allMails(long id,long userId,String reference){
        return api.allMails(id,userId,reference);
    }
    public Observable<List<MailResponse>> sendMails(long id,long userId){
        return api.sendMails(id,userId);
    }
    public Observable<List<MailResponse>> traitMails(String reference,long userId){
        return api.traitMails(reference,userId);
    }
    public Observable<List<MailResponse>> favoriteMails(long id,long userId){
        return api.favoriteMails(id,userId);
    }
    public Observable<List<MailResponse>> receivedMails(String reference,long userId){
        return api.receivedMails(reference,userId);
    }
    public Observable<List<MailResponse>> archiveMails(long id,long userId,String reference){
        return api.archiveMails(id,userId,reference);
    }
    public Observable<List<MailResponse>> toTraitMails(String reference,long userId){
        return api.toTraitMails(reference,userId);
    }
    public Completable updateMail(long id,List<MultipartBody.Part> file,RequestBody requestBody){return api.updateMail(id,file,requestBody);}
    public Observable<List<String>> reference(long id){return api.references(id);}
    public Completable updateReceivedMails(long id, ReceivedMail mail) {
        return api.updateReceivedMail(id,mail);
    }

    //Authentication
    public Observable<Response<Void>> logout(){return api.logout();}

    //User
    public Observable<UserResponse> getUserById(long id){return api.getUserById(id);}
    public Observable<List<UserResponse>> getUsers(){return api.getUsers();}
    public Completable updatePassword(long id, User user){return api.updatePassword(id,user);}
    public Completable updateNotification(long id){return api.updateNotification(id);}
    public Completable generatePassword(long id){return api.generatePassword(id);}
    public Completable updateToken(long id, String token){return api.updateToken(id,token);}
    public Completable updateUserInfo(long id, User user){return api.updateUserInfo(id,user);}
    public Completable updateUserPhoto(long id, MultipartBody.Part m){return api.updateUserPhoto(id,m);}
    public Completable createUser(User user){return api.createUser(user);}

    //Structure
    public Observable<List<Structure>> getStructures(){return api.getStructures();}
    public Observable<Structure> getStructure(long id){return api.getStructure(id);}
    public Completable updateStructure(long id,Structure structure){return api.updateStructure(id,structure);}
    public Completable addStructure (Structure structure){return api.addStructure(structure);}
    public Observable<List<String>> getStructureDesignation(){return api.getStructureDesignation();}

    //Category
    public Observable<List<CategoryResponse>> getCategories(){
        return api.getCategories();
    }
    public Observable<List<String>> getCategoryDesignation(long structure_id){
        return api.getCategoryDesignation(structure_id);
    }
    public Observable<List<String>> getArrivedCategoryDesignation(long structure_id){
        return api.getArrivedCategoryDesignation(structure_id);
    }
    public Observable<List<CategoryInfo>> getArrivedCategoryInfo(long id){
        return api.getArrivedCategoryInfo(id);
    }
    public Observable<List<CategoryInfo>> getSendCategoryInfo(long id){
        return api.getSendCategoryInfo(id);
    }
    public Completable addCategory(Category category){return  api.addCategory(category);}
    public Completable updateCategory(long id,Category category){return api.updateCategory(id,category);}

    //Comment
    public Observable<List<CommentResponse>> getComments(long id){return api.getComments(id);}

    //Trace
    public Observable<List<TraceResponse>> getTraces(){return api.getTraces();}

    //Employee
    public Completable addEmployee(Employee employee){return  api.addEmployee(employee);}
    public Observable<List<EmployeeResponse>> getEmployees(long id){
        return api.getEmployees(id);
    }
    public Completable updateEmployee(long id,Employee employee){return api.updateEmployee(id,employee);}
    public Completable updateRecuperation(long id,int recuperation){return api.updateRecuperation(id,recuperation);}

    //Attendance
    public Completable addAttendance(Attendance attendance){ return api.addAttendance(attendance);}

    //Report
    public Observable<List<Report>> getReports(long id){return api.getReports(id);}
    public Observable<List<Report>> getReportsByStructure(long id){return api.getReportsByStructure(id);}
    public Observable<Report> getDailyReport(long id){return api.getDailyReport(id);}
    public Completable updateApproved(long id){return api.updateApproved(id);}

    //email
    public Completable sendEmail(Email email){ return api.sendEmail(email);}

    //Favorite
    public Completable insertFavorite(FavoritePk favorite) {
        return api.insertFavorite(favorite);
    }
    public Completable updateFavorite(FavoritePk favorite) {
        return api.updateFavorite(favorite);
    }

    //Archive
    public Completable insertArchive(ArchivePk archive) {
        return api.insertArchive(archive);
    }
    public Completable updateArchive(ArchivePk archive) {
        return api.updateArchive(archive);
    }

    //Trait
    public Completable insertTrait(TraitPk trait) {
        return api.insertTrait(trait);
    }


    public static void setINSTANCE(ApiCall INSTANCE) {
        ApiCall.INSTANCE = INSTANCE;
    }
}
