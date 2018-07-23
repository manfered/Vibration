package com.gilasak.larzeafzar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class GraphFileLoadActivity extends AppCompatActivity {

    Button dialogLoadCancelButton;
    TextView textViewLoadFileEmpty;
    RecyclerView dialogLoadFileRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_load_graph_review);

        dialogLoadCancelButton = (Button) findViewById(R.id.dialogLoadCancelButton);
        textViewLoadFileEmpty = (TextView) findViewById(R.id.textViewLoadFileEmpty);
        dialogLoadFileRecyclerView = (RecyclerView) findViewById(R.id.dialogLoadFileRecyclerView);

        dialogLoadCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GraphFileLoadActivity.this , GraphActivityComplete.class);
                intent.putExtra("TYPE","Review");
                startActivity(intent);
                finish();
            }
        });

        File[] savedFilesList = getFilesDir().listFiles();
        ArrayList<GraphFile> graphFiles = new ArrayList<GraphFile>();
        for (File f : savedFilesList)
        {
            GraphFile graphFile = new GraphFile(f.getName());
            graphFiles.add(graphFile);
        }

        LinearLayoutManager llm = new LinearLayoutManager(GraphFileLoadActivity.this);
        dialogLoadFileRecyclerView.setLayoutManager(llm);
        dialogLoadFileRecyclerView.setHasFixedSize(true);
        dialogLoadFileRecyclerView.setAdapter(new GraphFileAdapter(graphFiles));

        if(savedFilesList.length>0){
            dialogLoadFileRecyclerView.setVisibility(View.VISIBLE);
        }
        else{
            textViewLoadFileEmpty.setVisibility(View.VISIBLE);
        }
    }

    public void listGotEmpty(){
        dialogLoadFileRecyclerView.setVisibility(View.GONE);
        textViewLoadFileEmpty.setVisibility(View.VISIBLE);
    }
}
