package com.gilasak.larzeafzar;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class GraphFileAdapter extends RecyclerView.Adapter<GraphFileAdapter.GraphFileViewHolder> {

    private List<GraphFile> graphFileList;

    public GraphFileAdapter(List<GraphFile> _graphFileList){
        this.graphFileList = _graphFileList;
    }

    @Override
    public GraphFileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View GraphFileView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_graph_file,parent,false);
        return new GraphFileViewHolder(GraphFileView) ;
    }

    @Override
    public void onBindViewHolder(final GraphFileViewHolder holder, final int position) {
        final GraphFile graphFile = graphFileList.get((position));

        holder.recyclerItemNameTextBox.setText(graphFile.fileName);
        holder.recyclerItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(holder.context , GraphActivityComplete.class);
                intent.putExtra("TYPE","Review");
                intent.putExtra("FILE",graphFile.fileName);
                holder.context.startActivity(intent);
                ((Activity)holder.context).finish();
            }
        });

        holder.recyclerItemLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final Dialog dialogDelete = new Dialog(holder.context);
                dialogDelete.setContentView(R.layout.dialog_delete_graph_file);

                Button dialogDeleteCancelButton = (Button) dialogDelete.findViewById(R.id.dialogDeleteCancelButton);
                Button dialogDeleteDeleteButton = (Button) dialogDelete.findViewById(R.id.dialogDeleteDeleteButton);
                TextView dialogDeleteGraphFileTextView = (TextView) dialogDelete.findViewById(R.id.dialogDeleteGraphFileTextView);

                dialogDeleteGraphFileTextView.setText(graphFile.fileName);

                dialogDeleteCancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogDelete.dismiss();
                    }
                });

                dialogDeleteDeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //delete file
                        GraphIO graphIO = new GraphIO(holder.context);
                        GraphIOResult graphIOResult = graphIO.delete(graphFile.fileName);

                        if(graphIOResult.result){
                            graphFileList.remove(position);
                            notifyDataSetChanged();
                            //notifyItemRemoved(position);
                            dialogDelete.dismiss();
                            if(graphFileList.size() == 0)
                                ((GraphFileLoadActivity)holder.context).listGotEmpty();
                        }
                        else{
                            dialogDelete.dismiss();

                            final Dialog dialogDeleteError = new Dialog(holder.context);
                            dialogDeleteError.setContentView(R.layout.dialog_save_graph_result);

                            TextView dialogDeleteFileResultTextView = (TextView) dialogDeleteError.findViewById(R.id.dialogDeleteFileResultTextView);
                            Button dialogDeleteOkResultButton = (Button) dialogDeleteError.findViewById(R.id.dialogDeleteOkResultButton);

                            dialogDeleteFileResultTextView.setText(holder.context.getResources().getString(R.string.DialogDeleteFileGeneralError));
                            dialogDeleteOkResultButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogDeleteError.dismiss();
                                }
                            });

                        }
                    }
                });

                dialogDelete.show();

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return graphFileList.size();
    }


    public class GraphFileViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout recyclerItemLinearLayout;
        public TextView recyclerItemNameTextBox;
        public Context context;

        public GraphFileViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            recyclerItemNameTextBox = (TextView) itemView.findViewById(R.id.recyclerItemNameTextBox);
            recyclerItemLinearLayout = (LinearLayout) itemView.findViewById(R.id.recyclerItemLinearLayout);
        }
    }
}
