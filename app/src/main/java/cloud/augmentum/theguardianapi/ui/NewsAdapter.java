package cloud.augmentum.theguardianapi.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;
import cloud.augmentum.theguardianapi.R;
import cloud.augmentum.theguardianapi.api.model.Body;
import cloud.augmentum.theguardianapi.api.model.Result;

public class NewsAdapter extends ArrayAdapter<Result> {

    private Context mContext;
    private List<Result> newsList;
    private ViewGroup transitionContainer;
    // This variable holds whether the card is extended or not
    private boolean isExtended = false;


    // Public constructor for the adapter
    public NewsAdapter(Context context, List<Result> list) {
        super(context, 0, list);
        mContext = context;
        newsList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        }
        
        // Set the current news item in the list
        Result currentNews = newsList.get(position);

        // Find reference to the views in the list item
        TextView name = listItem.findViewById(R.id.list_item_header);
        TextView category = listItem.findViewById(R.id.list_item_category);
        final TextView body = listItem.findViewById(R.id.list_item_body);
        Button learnMoreButton = listItem.findViewById(R.id.list_item_btn);
        transitionContainer = listItem.findViewById(R.id.list_item_transition_container);

        // Parse the body text of the current news item
        List<Body> currentNewsBodyElements = currentNews.getBlocks().getBody();
        Body currentNewsBody = currentNewsBodyElements.get(0);
        // The full body text will be used when the user has expanded the CardView
        final String bodyTextFull = currentNewsBody.getBodyTextSummary();
        // The summary of the body text will be used when the CardView is retracted
        final String bodyTextSummary = bodyTextFull.substring(0, Math.min(bodyTextFull.length(), CatalogActivity.BODY_MAX_TEXT_LENGHT)).concat("...");

        // Parse the name and category for the current news item
        String nameString = currentNews.getWebTitle();
        String categoryString = currentNews.getSectionName();

        // Set the parsed values
        name.setText(nameString);
        category.setText(categoryString);
        body.setText(bodyTextSummary);

        // onClickListener for the button to extend and retract the CardView 
        learnMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExtended){
                    retractCardView(body, bodyTextSummary);
                } else {
                    extendCardView(body, bodyTextFull);
                }
            }
        });

        // onClickListener for the body to retract the extended CardView
        body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExtended){
                    retractCardView(body, bodyTextSummary);
                }
            }
        });

        return listItem;
    }

    /**
     * Helper method to retract the current CardView
     * @param body is the main body view of the current card
     * @param bodyTextSummary is the summary of the body text we'll set the body to
     */
    private void retractCardView(TextView body, String bodyTextSummary){
        TransitionManager.beginDelayedTransition(transitionContainer, new ChangeBounds().setDuration(500));
        body.setText(bodyTextSummary);
        isExtended = false;
    }

    /**
     * Helper method to extend the current CardView
     * @param body is the main body view of the current card
     * @param bodyTextFull is the full body text we'll set the body to
     */
    private void extendCardView(TextView body, String bodyTextFull){
        TransitionManager.beginDelayedTransition(transitionContainer,new ChangeBounds().setDuration(500));
        body.setText(bodyTextFull);
        isExtended = true;
    }
}
