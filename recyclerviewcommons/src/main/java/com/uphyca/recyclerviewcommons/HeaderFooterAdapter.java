/*
 * Copyright (C) 2016 uPhyca Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.uphyca.recyclerviewcommons;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter with single header, footer
 */
public abstract class HeaderFooterAdapter<T, VH extends RecyclerView.ViewHolder> extends ArrayAdapter<T, RecyclerView.ViewHolder> {

    protected abstract VH onCreateItemViewHolder(ViewGroup parent);

    protected abstract void onBindItemViewHolder(VH holder, int position);

    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    protected void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Empty
    }

    protected RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent) {
        return null;
    }

    protected void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Empty
    }

    private static final int ITEM_VIEW_TYPE_ITEM = 0;
    private static final int ITEM_VIEW_TYPE_HEADER = 1;
    private static final int ITEM_VIEW_TYPE_FOOTER = 2;

    private boolean hasHeader;
    private boolean hasFooter;
    private boolean showFooter;

    public HeaderFooterAdapter(boolean hasHeader, boolean hasFooter) {
        this(new ArrayList<T>(), hasHeader, hasFooter);
    }

    public HeaderFooterAdapter(@NonNull List<T> objects, boolean hasHeader, boolean hasFooter) {
        super(objects);
        this.hasHeader = hasHeader;
        this.hasFooter = hasFooter;
        showFooter = this.hasFooter;
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_VIEW_TYPE_HEADER:
                return onCreateHeaderViewHolder(parent);
            case ITEM_VIEW_TYPE_ITEM:
                return onCreateItemViewHolder(parent);
            case ITEM_VIEW_TYPE_FOOTER:
                return onCreateFooterViewHolder(parent);
        }
        return null;
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case ITEM_VIEW_TYPE_HEADER:
                onBindHeaderViewHolder(holder, position);
                break;
            case ITEM_VIEW_TYPE_ITEM:
                onBindItemViewHolder((VH) holder, position);
                break;
            case ITEM_VIEW_TYPE_FOOTER:
                onBindFooterViewHolder(holder, position);
                break;
            default:
                // Should not happen
                // TODO: Add an assert?
        }
    }

    public void showFooter() {
        if (!showFooter && hasFooter) {
            showFooter = true;
            notifyItemInserted(getHeadersCount() + getAdapterItemCount());
        }
    }

    public void hideFooter() {
        if (showFooter) {
            showFooter = false;
            notifyItemRemoved(getHeadersCount() + getAdapterItemCount());
        }
    }

    private int getHeadersCount() {
        return hasHeader ? 1 : 0;
    }

    private int getFootersCount() {
        return showFooter ? 1 : 0;
    }

    private int getAdapterItemCount() {
        return super.getItemCount();
    }

    @Override
    public final int getItemCount() {
        return getHeadersCount() + getFootersCount() + getAdapterItemCount();
    }

    @Override
    public final int getItemViewType(int position) {
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return ITEM_VIEW_TYPE_HEADER;
        }
        int itemPosition = position - numHeaders;
        if (itemPosition < getAdapterItemCount()) {
            return ITEM_VIEW_TYPE_ITEM;
        }
        return ITEM_VIEW_TYPE_FOOTER;
    }

    @Override
    protected int getPositionOffset() {
        return getHeadersCount();
    }

    public static class HeaderFooterHolder extends RecyclerView.ViewHolder {

        public HeaderFooterHolder(View itemView) {
            super(itemView);
        }
    }
}
