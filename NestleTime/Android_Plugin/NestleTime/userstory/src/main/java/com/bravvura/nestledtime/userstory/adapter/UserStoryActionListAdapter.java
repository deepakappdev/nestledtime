package com.bravvura.nestledtime.userstory.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.userstory.model.UserStoryElementType;

import java.util.ArrayList;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 23-03-2018.
 */

public class UserStoryActionListAdapter extends RecyclerView.Adapter<UserStoryActionListAdapter.ActionButtonViewHolder> {
    private final UserStoryActionButtonListener buttonListener;

    public void addActionButtonItem(UserStoryActionButton userStoryActionButton) {
        actionButtons.add(userStoryActionButton);
        notifyItemInserted(actionButtons.size() - 1);
    }

    public static class UserStoryActionButton {
        String text;
        int resourceId;
        UserStoryElementType elementType;

        public UserStoryActionButton(String text, int resourceId, UserStoryElementType elementType) {
            this.text = text;
            this.resourceId = resourceId;
            this.elementType = elementType;
        }
    }

    ArrayList<UserStoryActionButton> actionButtons = new ArrayList<>();

    public interface UserStoryActionButtonListener {
        void onClick(UserStoryElementType elementType);
    }

    public UserStoryActionListAdapter(UserStoryActionButtonListener buttonListener) {
        this.buttonListener = buttonListener;
    }

    @Override
    public ActionButtonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ActionButtonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_action_button, parent, false));
    }

    @Override
    public void onBindViewHolder(ActionButtonViewHolder holder, int position) {
        holder.populateItem(actionButtons.get(position));
    }

    @Override
    public int getItemCount() {
        return actionButtons.size();
    }

    class ActionButtonViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageAction;
        private final TextView textAction;
        private UserStoryActionButton actionButton;

        public ActionButtonViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonListener.onClick(actionButton.elementType);
                }
            });
            imageAction = itemView.findViewById(R.id.image_action);
            textAction = itemView.findViewById(R.id.text_action);
        }

        public void populateItem(UserStoryActionButton actionButton) {
            this.actionButton = actionButton;
            imageAction.setImageResource(actionButton.resourceId);
            textAction.setText(actionButton.text);
        }
    }
}
