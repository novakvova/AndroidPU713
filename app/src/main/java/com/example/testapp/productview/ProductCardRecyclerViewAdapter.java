package com.example.testapp.productview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.productview.click_listeners.OnDeleteListener;
import com.example.testapp.productview.click_listeners.OnEditListener;
import com.example.testapp.network.ImageRequester;
import com.example.testapp.network.ProductEntry;

import java.util.Date;
import java.util.List;

/**
 * Adapter used to show a simple grid of products.
 */
public class ProductCardRecyclerViewAdapter extends RecyclerView.Adapter<ProductCardViewHolder> {

    private List<ProductEntry> productList;
    private ImageRequester imageRequester;
    private OnDeleteListener deleteListener;
    private OnEditListener onEditListener;
    private Context context;


    ProductCardRecyclerViewAdapter(List<ProductEntry> productList, OnEditListener editListener, OnDeleteListener deleteListener,Context context) {
        this.productList = productList;
        this.deleteListener = deleteListener;
        this.onEditListener = editListener;
        this.context=context;
        imageRequester = ImageRequester.getInstance();
    }

    @NonNull
    @Override
    public ProductCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.product_card, parent, false);
        return new ProductCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCardViewHolder holder, final int position) {
        if (productList != null && position < productList.size()) {
            ProductEntry product = productList.get(position);
            holder.productTitle.setText(product.title);
            holder.productPrice.setText(product.price);

            holder.getView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteListener.deleteItem(productList.get(position));
                    return true;
                }
            });

            holder.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEditListener.editItem(productList.get(position).id);
                }
            });

            int i = (int) (new Date().getTime()/1000);

            imageRequester.setImageFromUrl(holder.productImage, product.url+"?data="+i);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
