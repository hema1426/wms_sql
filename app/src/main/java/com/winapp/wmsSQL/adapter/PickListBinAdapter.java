package com.winapp.wmsSQL.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.winapp.wmsSQL.R;
import com.winapp.wmsSQL.model.ItemBinLocation.ResponseDataBinLocItem;

import java.util.List;


public class PickListBinAdapter extends RecyclerView.Adapter<PickListBinAdapter.MyViewHolder> {
    private Context context;
    private List<ResponseDataBinLocItem> dataList;
    private PickListInvoiceClickListener pickListInvoiceClickListener;

    public PickListBinAdapter(Context context, List<ResponseDataBinLocItem> pickBinItemList) {
        this.context = context;
        this.dataList = pickBinItemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.picklist_bin_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.setData(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView snotxt,bintxt,binqtytxt;

        MyViewHolder(View itemView) {
            super(itemView);

            this.snotxt = (TextView) itemView.findViewById(R.id.picki_binSno_item);
            this.bintxt = (TextView) itemView.findViewById(R.id.picki_binCount_item);
            this.binqtytxt = (TextView) itemView.findViewById(R.id.picki_binQty_item);

//            picklistinvoicelay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (pickListInvoiceClickListener != null) {
//                        int pos = getAdapterPosition();
//                        if (pos != -1) {
//                            //card_bg.setBackgroundResource(R.color.colorPrimaryLight);
//                            // packageCategoryClickListener.packageCategorySelected(dataList.get(pos));
//                            for (int i = 0; i < dataList.size(); i++) {
//                                ResponseDataPickInvoiceItem respItem = dataList.get(i);
//                                respItem.setItemSelected(i == pos);
//                                dataList.set(i, respItem);
//                                notifyItemChanged(i);
//                            }
//
//                            pickListInvoiceClickListener.pickListInvoiceSelected(pos);
//
//                        }
//                    }
//                }
//            });
        }

        public void setData(ResponseDataBinLocItem pickBinItem) {
//            int index=1;
//            for (int i =0; dataList.size()>0;i++){
//                snotxt.setText(getPosition()+1);
//            }

//            int pos = getAdapterPosition()+1;
//            snotxt.setText(String.valueOf(pos));
//            pos++;
//Log.e("pos",""+String.valueOf(pos));
//            bintxt.setText(pickBinItem.getBinLocationCode());
//            binqtytxt.setText(pickBinItem.getOnHandQty());

      }
    }

    public interface  PickListInvoiceClickListener{
        void pickListInvoiceSelected(Integer position);
    }
}
