
package zebrafish.util;

import java.io.IOException;
import edu.pitt.cs1635group3.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

public abstract class AsyncServiceCall<P, G, R> extends AsyncTask<P, G, Throwable> {
	
	private Activity context;
	private int progressStringId;
	private int successStringId;
	private int failureStringId;
	private ProgressDialog progressDialog;
	private R resultRun;
	private boolean retry;
	private boolean retryEnabled;
	public AsyncServiceCall(Activity ctx) {
		if (ctx == null) {
			throw new IllegalArgumentException("Context must not be null.");
		}
		
		context = ctx;
		progressStringId = -1;
		successStringId = -1;
		failureStringId = -1;
		retryEnabled = true;
	}
	
	public int getProgressStringId() {
		return progressStringId;
	}

	public void setProgressStringId(int strId) {
		this.progressStringId = strId;
	}
	
	public int getSuccessStringId() {
		return successStringId;
	}
	
	public void setSuccessStringId(int strId) {
		this.successStringId = strId;
	}

	public int getFailureStringId() {
		return failureStringId;
	}

	public void setFailureStringId(int strId) {
		this.failureStringId = strId;
	}
	
	public void setRetry(boolean enabled) {
		retryEnabled = enabled;
	}
	
	protected Activity getContext() {
		return context;
	}

	@Override
	protected final void onPreExecute() {
		if (getProgressStringId() != -1) {
			progressDialog =
				ProgressDialog.show(
					context,
					"",
					context.getString(getProgressStringId()),
					false);
		}
		//
		onPreRun();
	}

	@Override
	protected final Throwable doInBackground(P... params) {
		Throwable error = null;
		
		do {
			try {
				if (!IOUtil.isOnline(context)) {
					return new IOException();
				}
				
				resultRun = run(params);
				
				return null;
			} catch (Throwable e) {
				if (retryEnabled) {
					retry(e);
				}
				error = e;
			}
		} while (retry);
		
		return error;
	}
	
	protected final void onPostExecute(Throwable result) {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		
		if (result != null) {
			if (!retryEnabled) {
				if (getFailureStringId() != -1) {
					UIUtil.showMessage(context, getFailureStringId());
				} else {
					UIUtil.showMessage(context, result);
				}
			}
			
			onFailedRun(result);
		} else {
			if (getSuccessStringId() != -1) {
				UIUtil.showMessage(context, getSuccessStringId());
			}
			
			onPostRun(resultRun);
		}
	};

	protected void onPreRun() {}
	protected abstract R run(P... params) throws IOException;
	protected void onPostRun(R result) {}
	protected void onFailedRun(Throwable result) {}

	private void retry(Throwable exception) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		builder.setTitle(context.getString(edu.pitt.cs1635group3.R.string.app_name));
		builder.setMessage(getRetryMessage(exception));
		builder.setCancelable(false);
		builder.setPositiveButton(
			context.getString(edu.pitt.cs1635group3.R.string.yes),
			new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				retry = true;
				
				synchronized (AsyncServiceCall.this) {
					AsyncServiceCall.this.notify();
				}
			}
		});
		builder.setNegativeButton(
			context.getString(edu.pitt.cs1635group3.R.string.no),
			new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				retry = false;
				
				synchronized (AsyncServiceCall.this) {
					AsyncServiceCall.this.notify();
				}
			}
		});
		
		context.runOnUiThread(new Runnable() {
			public void run() {
				builder.create().show();
			}
		});
		
		synchronized (AsyncServiceCall.this) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	

	private String getRetryMessage(Throwable exception) {
		String retryMsg = context.getString(edu.pitt.cs1635group3.R.string.try_again);
		
		if (getFailureStringId() != -1) {
			retryMsg =
				context.getString(getFailureStringId()) + " " + retryMsg;
		} else {
			retryMsg =
				context.getString(
					UIUtil.getMessageId(exception)) + " " +retryMsg;
		}
		
		return retryMsg;
	}
}
