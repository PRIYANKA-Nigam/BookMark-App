package com.example.bookmark;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
Context context;DBHelper dbHelper;private ListView listView;private TextView textView;private Button button;
private List<Book> books;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        dbHelper=new DBHelper(context);
        listView=(ListView)findViewById(R.id.ll);
        textView=(TextView)findViewById(R.id.textView4);
        button=(Button)findViewById(R.id.button);
        int count=dbHelper.countBooks();
        textView.setText("You have "+count +" books");
        books=new ArrayList<>();
        books=dbHelper.getAllBooks();
        BookAdapter bookAdapter=new BookAdapter(context,R.layout.single_book,books);
        listView.setAdapter(bookAdapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,AddBook.class));
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book=books.get(position);
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                String finishRead=new SimpleDateFormat("dd/MM/yy").format(book.getStarted());
                if(book.getFinished()==0){
                    finishRead="Still Reading";

                }
                builder.setTitle(book.getTitle()).setMessage("By "+book.getAuthor()+
                        "\n\n\n" +new SimpleDateFormat("dd/MM/yy").format(book.getStarted())+"\n"+finishRead +"\n\n"+ book.getReview()+ "\n")
                        .setPositiveButton("Finished Reading", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        book.setFinished(System.currentTimeMillis());
                        dbHelper.updateBook(book);
                        startActivity(new Intent(context,MainActivity.class));

                    }
                })
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbHelper.deleteBooks(book.getId());
                                startActivity(new Intent(context,MainActivity.class));
                            }
                        }).setNeutralButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(context,AddBook.class);
                        intent.putExtra("id",String.valueOf(book.getId()));
                        startActivity(intent);
                    }
                }).show();

            }
        });
    }
}