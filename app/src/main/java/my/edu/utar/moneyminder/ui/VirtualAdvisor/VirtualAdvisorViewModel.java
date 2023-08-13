package my.edu.utar.moneyminder.ui.VirtualAdvisor;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class VirtualAdvisorViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public VirtualAdvisorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is VirtualAdvisor fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}