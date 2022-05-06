package com.example.projectepis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private String recibirUserId, nombre, apellido, imagen;
    private TextView nombreUsuario, apellidoUsuario;
    private ImageView imagenUsuario, botonEnviar, botonArchivo;
    private Toolbar toolbar;
    private EditText mensaje;
    private DatabaseReference RootRef;
    private FirebaseAuth auth;
    private String EnviarUserId;

    private final List<Mensajes> mensajesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MensajeAdapter mensajeAdapter;
    private RecyclerView recyclerView;
    private String CurrentTime, CurrentDate;
    private String check="", myUrl="";
    private StorageTask uploadTask;
    private Uri fileUri;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        auth=FirebaseAuth.getInstance();
        EnviarUserId=auth.getCurrentUser().getUid();
        RootRef= FirebaseDatabase.getInstance().getReference();
        recibirUserId = getIntent().getExtras().get("user_id").toString();
        nombre = getIntent().getExtras().get("user_name").toString();
        apellido = getIntent().getExtras().get("user_apellido").toString();
        imagen = getIntent().getExtras().get("user_imagen").toString();
        IniciarLayout();
        nombreUsuario.setText(nombre);
        apellidoUsuario.setText(apellido);
        Picasso.get().load(imagen).placeholder(R.drawable.user).into(imagenUsuario);
        dialog = new ProgressDialog(this);
        mensajeAdapter = new MensajeAdapter(mensajesList);
        recyclerView =(RecyclerView) findViewById(R.id.listamensajesrecycler);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mensajeAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        RootRef.child("Mensajes").child(EnviarUserId).child(recibirUserId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Mensajes mensajes = snapshot.getValue(Mensajes.class);
                mensajesList.add(mensajes);
                mensajeAdapter.notifyDataSetChanged();

                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void IniciarLayout() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        CurrentDate = dateFormat.format(calendar.getTime());
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("hh:mm a");
        CurrentTime = dateFormat1.format(calendar.getTime());

        toolbar=(Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_chat_bar, null);
        actionBar.setCustomView(view);

        nombreUsuario=(TextView) findViewById(R.id.usuario_nombre);
        apellidoUsuario=(TextView) findViewById(R.id.usuario_apellido);
        imagenUsuario=(ImageView) findViewById(R.id.usuario_imagen);
        mensaje =(EditText) findViewById(R.id.mensaje);
        botonEnviar=(ImageView) findViewById(R.id.enviar_mensaje_boton);
        botonArchivo=(ImageView) findViewById(R.id.enviar_archivos_boton);
        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnviarMensaje();
            }
        });
        botonArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence opciones[] = new CharSequence[]{
                        "Imagenes",
                        "PDF",
                        "Txt"
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                builder.setTitle("Selecciona el tipo de archivo");
                builder.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0){
                            check = "imagen";
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("imagen/*");
                            startActivityForResult(intent.createChooser(intent, "Seleccionar Imagen"), 438);
                        }else if (i == 1){
                            check = "PDF";
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/pdf");
                            startActivityForResult(intent.createChooser(intent, "Seleccionar archivo PDF"), 438);

                        }else if(i==2){
                            check = "Txt";
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/txt");
                            startActivityForResult(intent.createChooser(intent, "Seleccionar archivo TXT"), 438);

                        }
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==438 && resultCode==RESULT_OK && data!=null&& data.getData() != null){

            dialog.setTitle("Enviando Imagen");
            dialog.setMessage("Estamos enviando tu imagen");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            fileUri = data.getData();

            if(!check.equals("imagen")){
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Documentos");
                String mensajeEnviadoRef = "Mensajes/"+ EnviarUserId+"/"+recibirUserId;
                String mensajeRecibidoRef = "Mensajes/"+ recibirUserId+"/"+EnviarUserId;

                DatabaseReference usuarioMensajeRef = RootRef.child("Mensajes").child(EnviarUserId).child(recibirUserId).push();
                String mensajePushId = usuarioMensajeRef.getKey();

                final StorageReference filePath = storageReference.child(mensajePushId+"."+check);

                filePath.putFile(fileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            uploadTask=filePath.putFile(fileUri);
                            uploadTask.continueWithTask(new Continuation() {
                                @Override
                                public Object then(@NonNull Task task) throws Exception {
                                    if(!task.isSuccessful()){
                                        throw task.getException();
                                    }return filePath.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()){
                                        Uri downloadUriT = task.getResult();
                                        myUrl = downloadUriT.toString();
                                        Map mensajeTxt = new HashMap();
                                        mensajeTxt.put("mensaje", myUrl);
                                        mensajeTxt.put("tipo", check);
                                        mensajeTxt.put("de", EnviarUserId);
                                        mensajeTxt.put("para", recibirUserId);
                                        mensajeTxt.put("mensajeID", mensajePushId);
                                        mensajeTxt.put("fecha", CurrentDate);
                                        mensajeTxt.put("hora", CurrentTime);
                                        Map mensajeTxtFull = new HashMap();
                                        mensajeTxtFull.put(mensajeEnviadoRef+"/"+mensajePushId, mensajeTxt);
                                        mensajeTxtFull.put(mensajeRecibidoRef+"/"+mensajePushId, mensajeTxt);
                                        RootRef.updateChildren(mensajeTxtFull);
                                        dialog.dismiss();
                                    }
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double p = (100.0*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        dialog.setMessage((int) p + " % Subido...");
                    }
                });



            } else if(check.equals("imagen")){
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Imagenes");
                String mensajeEnviadoRef = "Mensajes/"+ EnviarUserId+"/"+recibirUserId;
                String mensajeRecibidoRef = "Mensajes/"+ recibirUserId+"/"+EnviarUserId;

                DatabaseReference usuarioMensajeRef = RootRef.child("Mensajes").child(EnviarUserId).child(recibirUserId).push();
                String mensajePushId = usuarioMensajeRef.getKey();

                final StorageReference filePath = storageReference.child(mensajePushId+"."+"jpg");
                uploadTask=filePath.putFile(fileUri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Uri downloadUri = task.getResult();
                            myUrl = downloadUri.toString();
                            Map mensajeTxt = new HashMap();
                            mensajeTxt.put("mensaje", myUrl);
                            mensajeTxt.put("tipo", check);
                            mensajeTxt.put("de", EnviarUserId);
                            mensajeTxt.put("para", recibirUserId);
                            mensajeTxt.put("mensajeID", mensajePushId);
                            mensajeTxt.put("fecha", CurrentDate);
                            mensajeTxt.put("hora", CurrentTime);
                            Map mensajeTxtFull = new HashMap();
                            mensajeTxtFull.put(mensajeEnviadoRef+"/"+mensajePushId, mensajeTxt);
                            mensajeTxtFull.put(mensajeRecibidoRef+"/"+mensajePushId, mensajeTxt);
                            RootRef.updateChildren(mensajeTxtFull).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    dialog.dismiss();
                                    mensaje.setText("");
                                }
                            });
                        }
                    }
                });
            }else{
                dialog.dismiss();
                Toast.makeText(this, "No seleccinaste nada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void EnviarMensaje() {
        String mensajeText = mensaje.getText().toString();
        if(TextUtils.isEmpty(mensajeText)){
            Toast.makeText(this, "Escribe su mensaje", Toast.LENGTH_SHORT).show();
        }else{
            String mensajeEnviadoRef = "Mensajes/"+ EnviarUserId+"/"+recibirUserId;
            String mensajeRecibidoRef = "Mensajes/"+ recibirUserId+"/"+EnviarUserId;

            DatabaseReference usuarioMensajeRef = RootRef.child("Mensajes").child(EnviarUserId).child(recibirUserId).push();
            String mensajePushId = usuarioMensajeRef.getKey();

            Map mensajeTxt = new HashMap();
            mensajeTxt.put("mensaje", mensajeText);
            mensajeTxt.put("tipo", "texto");
            mensajeTxt.put("de", EnviarUserId);
            mensajeTxt.put("para", recibirUserId);
            mensajeTxt.put("mensajeID", mensajePushId);
            mensajeTxt.put("fecha", CurrentDate);
            mensajeTxt.put("hora", CurrentTime);
            Map mensajeTxtFull = new HashMap();
            mensajeTxtFull.put(mensajeEnviadoRef+"/"+mensajePushId, mensajeTxt);
            mensajeTxtFull.put(mensajeRecibidoRef+"/"+mensajePushId, mensajeTxt);
            RootRef.updateChildren(mensajeTxtFull).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    mensaje.setText("");
                }
            });
        }
    }
}