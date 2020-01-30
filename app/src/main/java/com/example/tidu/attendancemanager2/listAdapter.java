package com.example.tidu.attendancemanager2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class listAdapter extends RecyclerView.Adapter<listAdapter.listViewHolder> {
    Context mContext;
    List<SubjectInfo> itemList;
    DatabaseHelper mdb;
    public  onClickListnerPlusMinus onClickListnerPlusMinus;
    int state=2;

    public listAdapter(Context mContext, List<SubjectInfo> itemList,onClickListnerPlusMinus onClickListnerPlusMinus) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.onClickListnerPlusMinus=onClickListnerPlusMinus;
    }

    @NonNull
    @Override
    public listViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v = inflater.inflate(R.layout.recycler_list,viewGroup,false);
        return new listViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final listViewHolder holder, final int i) {
        holder.sub.setText(itemList.get(i).getName());
        holder.mini.setText(itemList.get(i).getMinimum());
        holder.pres.setText(itemList.get(i).getPres());
        int mi=Integer.parseInt(itemList.get(i).getPres());
        final int pre = Integer.parseInt(itemList.get(i).getPres());
        int ab = Integer.parseInt(itemList.get(i).getAbs());
        int tot1 = pre + ab;
        double npresent=pre;
        double nabsent=(tot1-pre);
        double npercent=npresent/(nabsent+npresent)*100;
        holder.status.setText(canbunk(npercent,npresent,nabsent));
     /*   if(tot1!=0){
            if (((pre*100)tot1)>=mi){
                int BackC=ContextCompat.getColor(mContext,R.color.safegreen);
                holder.current.setTextColor(BackC);
            }
            else if(((pre*100)/tot1)<mi)
            {
                int BackC = ContextCompat.getColor(mContext, R.color.red);
                holder.current.setTextColor(BackC);
            }}
        else {
            int BackC = ContextCompat.getColor(mContext, R.color.red);
            holder.current.setTextColor(BackC);
        }*/
        final String total = Integer.toString(tot1);
        holder.tot.setText(total);
        final String sub = (String) holder.sub.getText();
        final int a =ab++;

        if(tot1!=0) {
            double cu = (Math.round(((1.0 * pre) / tot1) * 10000)) / 100;
            holder.current.setText(cu + "");
        }
        else
        {
            holder.current.setText("N/A");
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences prefs = mContext.getSharedPreferences("xyz", Context.MODE_PRIVATE);

                Intent intent = new Intent(mContext, detailsSubject.class);

                intent.putExtra("IName", itemList.get(i).getName());
                intent.putExtra("IMin", itemList.get(i).getMinimum());
                intent.putExtra("Ipres", itemList.get(i).getPres());
                intent.putExtra("ITot",total);
                intent.putExtra("Id",Integer.toString(itemList.get(i).getId()));
                intent.putExtra("IPer",itemList.get(i).getCurrent());

                mContext.startActivity(intent);
            }


        });
        holder.presatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state=1;
                holder.undo.setEnabled(false);
                holder.undo.setVisibility(View.INVISIBLE);
                int pre1 = Integer.parseInt((String) holder.pres.getText());

                int total1 = Integer.parseInt((String) holder.tot.getText());
                pre1++;
                total1++;
                holder.pres.setText(pre1+"");
                holder.tot.setText(total1+"");
                double npresent=pre1;
                double nabsent=(total1-pre1);
                double npercent=npresent/(nabsent+npresent)*100;
                holder.status.setText(canbunk(npercent,npresent,nabsent));
                if(total1!=0) {
                    double cu = (Math.round(((1.0 * pre1) / total1) * 10000)) / 100;
                    holder.current.setText(cu + "");
                }
                else
                {
                    holder.current.setText("N/A");
                }
                onClickListnerPlusMinus.onClickedPlus(i,sub,pre1);

                Intent intent=new Intent(mContext,MainActivity.class);
                mContext.startActivity(intent);


            }
        });
        holder.absatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.undo.setEnabled(false);
                holder.undo.setVisibility(View.INVISIBLE);
                state=0;
                int total1 = Integer.parseInt((String) holder.tot.getText());
                int ab = Integer.parseInt(itemList.get(i).getAbs());
                int pre1 = Integer.parseInt((String) holder.pres.getText());
                total1++;
                ab++;
                holder.tot.setText(total1+"");
                double npresent=pre1;
                double nabsent=(total1-pre1);
                double npercent=npresent/(nabsent+npresent)*100;
                holder.status.setText(canbunk(npercent,npresent,nabsent));

                if(total1!=0) {
                    double cu = (Math.round(((1.0 * pre1) / total1) * 10000)) / 100;
                    holder.current.setText(cu + "");
                }
                else
                {
                    holder.current.setText("N/A");
                }
                onClickListnerPlusMinus.onClickedMinus(i,sub,ab);

                Intent intent=new Intent(mContext,MainActivity.class);
                mContext.startActivity(intent);



            }
        });
        holder.undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               }
        });
    }

    @Override
    public int getItemCount() {
        itemList.size();
        return itemList.size();
    }
    public void updateData(List<SubjectInfo> viewModels) {
        itemList.clear();
        itemList.addAll(viewModels);
        notifyDataSetChanged();
    }






    public class listViewHolder extends RecyclerView.ViewHolder{
        TextView id,sub,mini,pres,tot,current;
        RelativeLayout parentLayout;
        CircleImageView presatt,absatt,undo;
        TextView status;
        public listViewHolder(@NonNull View itemView) {
            super(itemView);
            sub = (TextView) itemView.findViewById(R.id.subject);
            mini = (TextView) itemView.findViewById(R.id.minAtten);
            pres = (TextView) itemView.findViewById(R.id.pres);
            tot = (TextView) itemView.findViewById(R.id.total);
            parentLayout=(RelativeLayout)itemView.findViewById(R.id.parent);
            presatt = itemView.findViewById(R.id.presAtt);
            absatt =itemView.findViewById(R.id.absAtt);
            current = (TextView)itemView.findViewById(R.id.Current);
            status=itemView.findViewById(R.id.status);
            undo=itemView.findViewById(R.id.undo);





        }

    }
    public String canbunk(Double cpercent,Double cpresent,Double cabsent)
    { double caim;
        SharedPreferences editor = mContext.getSharedPreferences("xyz", Context.MODE_PRIVATE);
        if(editor.contains("minatt"))
            caim=  Double.parseDouble(editor.getString("minatt",null));
        else
            caim=(double)75;
        String cbunk;
        if(cpercent>caim)
        {
            double value;
            value=Math.floor((100*(cabsent+cpresent)-caim*(cabsent+cpresent)-100*cabsent)/caim);
            if(value==0.0)
                cbunk="Don't miss next lecture.";
            else
                cbunk="You can Bunk next "+String.format("%.0f",value)+" lectures.";
        }
        else
        if(cpercent<caim)
        {
            double value;
            value=Math.ceil(((cpresent+cabsent)*caim-100*cpresent)/(100-caim));
            cbunk="You must attend next "+String.format("%.0f",value)+" lectures.";
        }
        else
        {
            cbunk="Don't miss next lecture.";
        }
        return cbunk;
    }
}
