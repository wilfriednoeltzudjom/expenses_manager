package com.thetechshrine.expensemanager.utils;

import android.content.Context;

import androidx.annotation.IntDef;

import com.thetechshrine.expensemanager.utils.modals.ErrorModal;
import com.thetechshrine.expensemanager.utils.modals.Modal;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ModalFactory {

    public static final int ERROR = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ ERROR })
    @interface Type {}

    public static Modal create(@Type int type, Context context, Modal.OnModalChangeListener listener) {
        switch (type) {
            case ERROR:
                return new ErrorModal(context);
        }

        return null;
    }
}
