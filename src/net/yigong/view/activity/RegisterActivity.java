package net.yigong.view.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import net.yigong.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.marvinlabs.widget.floatinglabel.itempicker.FloatingLabelItemPicker;
import com.marvinlabs.widget.floatinglabel.itempicker.FloatingLabelItemPicker.OnItemPickerEventListener;
import com.marvinlabs.widget.floatinglabel.itempicker.ItemPickerListener;
import com.marvinlabs.widget.floatinglabel.itempicker.StringPickerDialogFragment;

@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseActivity implements ItemPickerListener<String>,OnItemPickerEventListener<String>{

	@ViewById(R.id.picker)
	protected FloatingLabelItemPicker picker;
	
	@SuppressWarnings("unchecked")
	@AfterViews
	void initViews(){
		picker.setItemPickerListener(this);
		picker.setAvailableItems(new ArrayList<String>(Arrays.asList("Item 2.1", "Item 2.2", "Item 2.3", "Item 2.4")));
		picker.setWidgetListener(new FloatingLabelItemPicker.OnWidgetEventListener<String>() {
            @Override
            public void onShowItemPickerDialog(FloatingLabelItemPicker source) {
                StringPickerDialogFragment itemPicker4 = StringPickerDialogFragment.newInstance(
                        source.getId(),
                        "Picker 4",
                        "OK", "Cancel",
                        false,
                        source.getSelectedIndices(),
                        new ArrayList<String>(source.getAvailableItems()));
//                itemPicker4.setTargetFragment(ItemWidgetsFragment.this, 0);
//                itemPicker4.show(getChildFragmentManager(), "ItemPicker4");
            }
        });
	}

	@Override
	public void onCancelled(int pickerId) {
		
	}

	@Override
	public void onItemsSelected(int pickerId, int[] selectedIndices) {
		
	}

	@Override
	public void onSelectionChanged(FloatingLabelItemPicker<String> source,
			Collection<String> selectedItems) {
		
	}
}
