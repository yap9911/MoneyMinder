package my.edu.utar.moneyminder.ui.transaction;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.widget.Button;

public class TransactionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TransactionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}