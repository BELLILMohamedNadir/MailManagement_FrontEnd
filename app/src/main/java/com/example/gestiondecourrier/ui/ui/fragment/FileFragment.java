package com.example.gestiondecourrier.ui.ui.fragment;

import static android.content.ContentValues.TAG;
import static com.example.gestiondecourrier.ui.ui.AuthenticationActivity.sp;
import static com.example.gestiondecourrier.ui.ui.MainActivity.navController;
import static com.example.gestiondecourrier.ui.ui.MainActivity.userId;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.gestiondecourrier.BuildConfig;
import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentFileBinding;
import com.example.gestiondecourrier.ui.deserializer.CommentDeserializer;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.pojo.Comment;
import com.example.gestiondecourrier.ui.pojo.CommentResponse;
import com.example.gestiondecourrier.ui.pojo.Folder;
import com.example.gestiondecourrier.ui.pojo.Mail;
import com.example.gestiondecourrier.ui.pojo.MailResponse;
import com.example.gestiondecourrier.ui.pojo.User;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewComment;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewFile;
import com.example.gestiondecourrier.ui.ui.viewmodel.CommentViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.MailViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.functions.Consumer;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;


public class FileFragment extends Fragment {
    FragmentFileBinding binding;
    ArrayList<CommentResponse> data;
    ArrayList<Bitmap> list;
    SnapHelper snapHelper;
    MailResponse mailResponse=null;
    MailViewModel mailViewModel=new MailViewModel();
    List<Folder> file;
    List<String> items;
    int cpt=1,position=0;
    PdfiumCore pdfiumCore;
    String folder="";
    RecyclerViewFile recyclerViewFile;
    RecyclerViewComment recyclerViewComment;
    SimpleDateFormat simpleDateFormat;
    static int pos=0;
    StompClient mStompClient=null;
    MutableLiveData<StompMessage> liveStompMessage=new MutableLiveData<>();
    MutableLiveData<Boolean> liveConnected=new MutableLiveData<>();
    int place=-1;
    String source="";
    CommentViewModel commentViewModel=new CommentViewModel();
    Feature feature=new Feature();
    public FileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mailViewModel= ViewModelProviders.of(requireActivity()).get(MailViewModel.class);
        commentViewModel=ViewModelProviders.of(requireActivity()).get(CommentViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentFileBinding.inflate(inflater,container,false);

        file=new ArrayList<>();
        items=new ArrayList<>();
        list=new ArrayList<>();
        initializeRecyclerViews();
        binding.shimmerComment.startShimmer();
        binding.shimmerComment.setVisibility(View.VISIBLE);

        if (getContext()!=null)
            pdfiumCore=new PdfiumCore(getContext());
        if (getArguments()!=null) {
            place = getArguments().getInt("mailPosition");
            source=getArguments().getString("mailSource","");
        }

        if (place!=-1 && source!=null && !source.isEmpty()){
            dataDependToTheSource();
        }


        // add the pdfs to file list with this way we can show it in the recyclerViewFile
        if (mailResponse!=null){
            for (int i = 0; i < mailResponse.getFileName().size(); i++) {
                items.add(mailResponse.getFileName().get(i));
                file.add(new Folder(mailResponse.getPaths().get(i), mailResponse.getBytes().get(i)));
            }
            if (!file.isEmpty())
                pdfToBitmap(file.get(0));
        }

        simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.getDefault());
        binding.txtNumberPageFile.setText(cpt+"/"+list.size());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerFromFile.setAdapter(adapter);

        binding.spinnerFromFile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                folder=(String) adapterView.getItemAtPosition(i);
                pos=i;
                pdfToBitmap(file.get(i));
                binding.txtNumberPageFile.setText("1/".concat(list.size()+""));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (adapterView.getItemAtPosition(0)!=null)
                    folder=(String) adapterView.getItemAtPosition(0);
            }
        });

        binding.imgInfoFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((source.equals("received") && mailViewModel.getLiveReceivedMails().getValue()!=null && mailViewModel.getLiveReceivedMails().getValue().get(place).getEntryDateReceived()==null)
                        || (source.equals("toTrait") && mailViewModel.getLiveToTraitMails().getValue()!=null && mailViewModel.getLiveToTraitMails().getValue().get(place).getEntryDateReceived()==null)){
                    AddReceivedFragment addReceivedFragment=new AddReceivedFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("mailPosition",place);
                    bundle.putString("source",source);
                    addReceivedFragment.setArguments(bundle);
                    addReceivedFragment.show(getChildFragmentManager(),"add_received_mail");
                }else{
                    Bundle bundle=new Bundle();
                    bundle.putInt("mailPosition",place);
                    bundle.putString("mailSource",source);
                    navController.navigate(R.id.mailDetailsFragment,bundle);
                }
            }
        });

        binding.imgAllFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("mailPosition",mailResponse.getPlaceInTheViewModel());
                bundle.putInt("folderPosition",mailResponse.getPlaceInTheViewModel());
                bundle.putString("source",source);
                navController.navigate(R.id.showPdfFragment,bundle);
            }
        });

        binding.imgNextFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cpt<list.size()){
                    binding.rvFile.scrollToPosition(++position);
                    cpt++;
                }
                binding.txtNumberPageFile.setText(String.valueOf(cpt+"/"+list.size()));
            }
        });

        binding.imgPreviousFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cpt>1){
                    binding.rvFile.scrollToPosition(--position);
                    cpt--;
                }
                binding.txtNumberPageFile.setText(String.valueOf(cpt+"/"+list.size()));
            }
        });

        binding.imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!binding.edtTypeCommentFile.getText().toString().isEmpty()) {

                    sendComment();

                }else
                    Toast.makeText(getActivity(), "Add text", Toast.LENGTH_SHORT).show();
            }
        });


        binding.imgBackFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               backToFragment();
            }
        });

        if (mailResponse!=null)
            getComments(mailResponse.getId());

        webSocketConnection();
        return binding.getRoot();
    }

    private void dataDependToTheSource() {
        switch (source){
            case "all" :
                if (mailViewModel.getLiveAllMails().getValue()!=null)
                    mailResponse=mailViewModel.getLiveAllMails().getValue().get(place);
                break;
            case "archive" :
                if (mailViewModel.getLiveArchiveMails().getValue()!=null)
                    mailResponse=mailViewModel.getLiveArchiveMails().getValue().get(place);
                break;
            case "favorite" :
                if (mailViewModel.getLiveFavoriteMails().getValue()!=null)
                    mailResponse=mailViewModel.getLiveFavoriteMails().getValue().get(place);
                break;
            case "received" :
                if (mailViewModel.getLiveReceivedMails().getValue()!=null)
                    mailResponse=mailViewModel.getLiveReceivedMails().getValue().get(place);
                break;
            case "send" :
                if (mailViewModel.getLiveSendMails().getValue()!=null)
                    mailResponse=mailViewModel.getLiveSendMails().getValue().get(place);
                break;
            case "toTrait" :
                if (mailViewModel.getLiveToTraitMails().getValue()!=null)
                    mailResponse=mailViewModel.getLiveToTraitMails().getValue().get(place);
                break;
            case "trait" :
                if (mailViewModel.getLiveTraitMails().getValue()!=null)
                    mailResponse=mailViewModel.getLiveTraitMails().getValue().get(place);
                break;
        }
    }

    private void backToFragment() {
        if (source!=null && !source.isEmpty()){
            switch (source){
                case "all" :
                    navController.popBackStack(R.id.allDocumentsFragment,false);
                    break;
                case "archive" :
                    navController.popBackStack(R.id.archiveFragment,false);
                    break;
                case "favorite" :
                    navController.popBackStack(R.id.favoriteFragment,false);
                    break;
                case "received" :
                    navController.popBackStack(R.id.receivedFragment,false);
                    break;
                case "send" :
                    navController.popBackStack(R.id.sendFragment,false);
                    break;
                case "toTrait" :
                    navController.popBackStack(R.id.toTraitFileFragment,false);
                    break;
                case "trait" :
                    navController.popBackStack(R.id.traitFileFragment,false);
                    break;
            }
        }
    }


    private void initializeRecyclerViews(){

        //recyclerView File
        recyclerViewFile=new RecyclerViewFile(list,binding.txtNumberPageFile);
        binding.rvFile.setAdapter(recyclerViewFile);
        binding.rvFile.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        binding.rvFile.setHasFixedSize(true);

        snapHelper= new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.rvFile);

        //recyclerView comment
        data=new ArrayList<>();

        recyclerViewComment=new RecyclerViewComment(data);
        binding.rvComment.setAdapter(recyclerViewComment);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.setReverseLayout(true);
        binding.rvComment.setLayoutManager(linearLayoutManager);
        binding.rvComment.setHasFixedSize(true);

        //refresh layout
        feature.refreshOptions(binding.refreshComment,binding.rvComment);

        binding.refreshComment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getComments(mailResponse.getId());
                binding.refreshComment.setRefreshing(false);
            }
        });

    }

    void getComments(long id){
        commentViewModel.getLiveGetComments().setValue(null);
        commentViewModel.getComments(id);
        getViewLifecycleOwner().getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_RESUME) {
                    getViewLifecycleOwner().getLifecycle().removeObserver(this);
                    commentViewModel.getLiveGetComments().observe(getViewLifecycleOwner(), new Observer<List<CommentResponse>>() {
                        @Override
                        public void onChanged(List<CommentResponse> comments) {
                            if (comments!=null) {
                                binding.shimmerComment.stopShimmer();
                                binding.shimmerComment.setVisibility(View.GONE);
                                if (!comments.isEmpty()) {
                                    binding.relativeNoComments.setVisibility(View.GONE);
                                    binding.rvComment.setVisibility(View.VISIBLE);
                                    recyclerViewComment.setData(comments);
                                } else {
                                    binding.relativeNoComments.setVisibility(View.VISIBLE);
                                    binding.rvComment.setVisibility(View.GONE);
                                }
                            }

                        }
                    });
                }
            }
        });
    }

    @SuppressLint("CheckResult")
    private void webSocketConnection(){

        //check if the connection is made
        Map<String,String> headers=new HashMap<>();

        headers.put("Authorization", "Bearer " + sp.getString(BuildConfig.TOKEN,null)); ///
        if (mStompClient==null)
            mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, BuildConfig.WEBSOCKET_URL,headers);
        if (mStompClient!=null && !mStompClient.isConnected())
            mStompClient.connect();


        if (mStompClient!=null && mStompClient.lifecycle()!=null){
            mStompClient.lifecycle().subscribe(lifecycleEvent -> {
                switch (lifecycleEvent.getType()) {

                    case OPENED:
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                liveConnected.setValue(true);
                            }
                        });
                        break;

                    case ERROR:
                        Log.e(TAG, " Error", lifecycleEvent.getException());
                        break;

                    case CLOSED:
                        Log.d(TAG, "Stomp connection closed");
                        break;
                }
            });
        }


        //subscribe to a topic
        liveConnected.observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.imgSend.setVisibility(View.VISIBLE);
                    binding.progressComment.setVisibility(View.GONE);
                    mStompClient.topic(BuildConfig.TOPIC+mailResponse.getId()).subscribe(new Consumer<StompMessage>() {
                        @Override
                        public void accept(StompMessage stompMessage) throws Exception {
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (stompMessage!=null) {
                                        binding.imgSend.setVisibility(View.VISIBLE);
                                        binding.progressComment.setVisibility(View.GONE);
                                        liveStompMessage.setValue(stompMessage);
                                    }else{
                                        binding.imgSend.setVisibility(View.GONE);
                                        binding.progressComment.setVisibility(View.VISIBLE);
                                        Toast.makeText(requireContext(), "connection failed", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });
                        }
                    });
                }else{
                    binding.imgSend.setVisibility(View.GONE);
                    binding.progressComment.setVisibility(View.VISIBLE);
                    Toast.makeText(requireContext(), "connection failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //handle the incoming messages
        liveStompMessage.observe(requireActivity(), new Observer<StompMessage>() {
            @Override
            public void onChanged(StompMessage stompMessage) {

                if (stompMessage!=null){
                    Gson gson=new GsonBuilder()
                            .registerTypeAdapter(CommentResponse.class,new CommentDeserializer())
                            .create();
                    CommentResponse commentResponse=gson.fromJson(stompMessage.getPayload(),CommentResponse.class);
                    if (commentResponse!=null && !commentResponse.getComment().isEmpty()) {
                        if (binding.relativeNoComments.getVisibility()==View.VISIBLE)
                            binding.relativeNoComments.setVisibility(View.GONE);
                        if (binding.rvComment.getVisibility()==View.GONE)
                            binding.rvComment.setVisibility(View.VISIBLE);

                        recyclerViewComment.addComment(commentResponse);
                    }
                }else
                    Toast.makeText(requireActivity(), "send message again", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void sendComment(){

        Comment comment = new Comment(binding.edtTypeCommentFile.getText().toString(),
                simpleDateFormat.format(Calendar.getInstance().getTime()), new Mail(mailResponse.getId()), new User(userId));
        // Convert the object to JSON string
        Gson gson = new Gson();
        String jsonString = gson.toJson(comment);
        mStompClient.send("/websocket.connect/"+mailResponse.getId(),jsonString).subscribe();
        binding.edtTypeCommentFile.setText("");

    }

    void pdfToBitmap(Folder folder){
        try {
            // Create a temporary file to write the PDF data to
            File tempFile = File.createTempFile("temp", ".pdf", requireActivity().getCacheDir());
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            outputStream.write(folder.getBytes());
            outputStream.close();

            // Open the temporary file using a ParcelFileDescriptor
            ParcelFileDescriptor pdfFileDescriptor = ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY);

            // Create a PdfRenderer and get the number of pages
            PdfRenderer pdfRenderer = new PdfRenderer(pdfFileDescriptor);
            int pageCount = pdfRenderer.getPageCount();

            // Extract each page and convert it to a bitmap
            list.clear();
            for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
                PdfRenderer.Page page = pdfRenderer.openPage(pageIndex);
                int width = page.getWidth();
                int height = page.getHeight();
                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                page.close();
                // Save the bitmap to a list or array of bitmaps for later use in a RecyclerView
                list.add(bitmap);
            }

            if (!list.isEmpty()) {
                recyclerViewFile.setData(list);
            }
            // Close the PdfRenderer and delete the temporary file
            pdfRenderer.close();

            tempFile.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //disconnect from websocket
        mStompClient.disconnect();
    }
}