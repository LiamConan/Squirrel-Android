package com.layne.squirrel.framework.autofill

import android.annotation.TargetApi
import android.os.Build
import android.os.Parcel
import android.view.autofill.AutofillId

@TargetApi(Build.VERSION_CODES.O)
data class ParsedStructure(
	var usernameId: AutofillId = AutofillId.CREATOR.createFromParcel(Parcel.obtain()),
	var passwordId: AutofillId = AutofillId.CREATOR.createFromParcel(Parcel.obtain())
)
