package com.winapp.KHDelivery.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.winapp.KHDelivery.R;
import com.winapp.KHDelivery.model.ProductsModel;

import java.util.ArrayList;


public class StockTakeAddAdapter1 extends RecyclerView.Adapter<StockTakeAddAdapter1.MyViewHolder> {
    private Context context;
    private ArrayList<ProductsModel> dataList;

    public StockTakeAddAdapter1(Context context, ArrayList<ProductsModel> transferInDetailsList) {
        this.context = context;
        this.dataList = transferInDetailsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.stock_take_add_item, parent, false));
    }
    public ArrayList<ProductsModel> getTransferInlist(){
        return  dataList;
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
        public TextView pdtnametxt, pdtcodetxt, stocktxt , uom_takel;
        public CardView transferinlay;
        public TextWatcher textWatcher;
        public EditText qtytxt;

        MyViewHolder(View itemView) {
            super(itemView);
            int pos = getAdapterPosition();
            this.pdtnametxt = itemView.findViewById(R.id.pdtname_transfer_item);
            this.pdtcodetxt = itemView.findViewById(R.id.pdtcode_transfer_item);
            this.qtytxt = itemView.findViewById(R.id.qty_transfer_item);
            this.stocktxt = itemView.findViewById(R.id.stock_transfer_item);
            this.transferinlay = itemView.findViewById(R.id.transferin_lay);
            this.uom_takel = itemView.findViewById(R.id.uom_take_item);
        }


        @SuppressLint("ClickableViewAccessibility")
        public void setData(ProductsModel transferInItem) {
            pdtnametxt.setText(transferInItem.getProductName());
            pdtcodetxt.setText(transferInItem.getProductCode());
            if(transferInItem.getQty() != null) {
               // if(transferInItem.getProductCode().equals(transferInItem.getProductCodeCopy())) {
                    qtytxt.setText(String.valueOf(transferInItem.getQty()));
               // }
            }
            stocktxt.setText(String.valueOf(transferInItem.getStockQty()));
            uom_takel.setText(transferInItem.getUomText());

            qtytxt.removeTextChangedListener(textWatcher);
            qtytxt.setSelection(qtytxt.getText().length());
            qtytxt.setSelectAllOnFocus(true);

            qtytxt.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    int pos = getAdapterPosition();
                    if (pos != -1) {
                        Log.e("editabl_s", "" + s.toString());
                        if (!s.toString().isEmpty()) {
                           // if (transferMode.equals("Stock Request")){
                                dataList.get(pos).setQty(s.toString());
//                  //              notifyDataSetChanged();
//                            }else {
//                                if (dataList.get(pos).getStockInHand() >= Integer.parseInt(s.toString())) {
//                                    dataList.get(pos).setQty(s.toString());
////                                    notifyDataSetChanged();
//                                } else {
//                                    qtytxt.setText("");
//                                    Toast.makeText(context, "Low stock !", Toast.LENGTH_SHORT).show();
//                                }
//                            }
                        }else {
                            dataList.get(pos).setQty("");
                        }
                    }
                }
            });
        }
    }

    public void updateList(ArrayList<ProductsModel> list){
        dataList = list;
        notifyDataSetChanged();
    }

    public interface  PickListInvoiceClickListener{
        void pickListInvoiceSelected(Integer position);
    }

}
