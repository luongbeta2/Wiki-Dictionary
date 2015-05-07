package com.dictionary.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.dictionary.aard.pro.R;
import com.dictionary.object.DictionaryObject;
import com.dictionary.utils.DLog;

public class DicDownloadAdapter extends BaseAdapter {

	private final String TAG = "Dic Adapter Download";
	private LayoutInflater inflater;
	private ArrayList<DictionaryObject> list;
	private Context context;
	private DLog log = new DLog();

	public DicDownloadAdapter(Context context, ArrayList<DictionaryObject> list) {
		this.context = context;
		this.list = list;
		// this.mInflater = LayoutInflater.from(context);
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;
		final DictionaryObject item = getItem(position);

		if (item.isSectionHeader()) {
			v = inflater.inflate(R.layout.item_dic_list_download_header, parent, false);

			v.setClickable(false);

			TextView header = (TextView) v.findViewById(R.id.section_header);
			header.setText(item.getGroupName());

			// if (item.getGroupName().equals("Wiktionary")) {
			// v.setBackgroundColor(context.getResources().getColor(R.color.plum_color_2));
			// header.setTextColor(context.getResources().getColor(R.color.white));
			// } else {
			// v.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
			// header.setTextColor(context.getResources().getColor(R.color.black));
			// }
			// v.invalidate();

		} else {
			v = inflater.inflate(R.layout.item_dictionary_to_download, parent, false);
			holder = new ViewHolder();

			holder.nameLb = (TextView) v.findViewById(R.id.name_lb);
			holder.sizeLb = (TextView) v.findViewById(R.id.size_lb);
			holder.downloadBtn = (Button) v.findViewById(R.id.download_btn);
			holder.sendEmailBtn = (Button) v.findViewById(R.id.send_btn);

			holder.nameLb.setText(item.getName());
			holder.sizeLb.setText(item.getSizeInfo());
			holder.downloadBtn.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_download), null, null, null);
			holder.downloadBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					log.e(TAG, item.getName());

					Uri uriUrl = Uri.parse(item.getSource());
					Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
					context.startActivity(launchBrowser);
				}
			});

			holder.sendEmailBtn.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_send_now), null, null, null);
			holder.sendEmailBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// sendEmail(context, "", item.getName() +
					// " - Link Download", item.getSource());

					log.e(TAG, item.getName() + " - Link Download: " + item.getSource() + "\n" + item.getSource_2());

					String msg = "";
					if (!TextUtils.isEmpty(item.getSource_2())) {
						msg = "(Link 1) \n " + item.getSource() + " \n\n " + "(Link 2) \n " + item.getSource_2();
						if (item.getSource_2().contains("magnet")) {
							msg = "(Link 1) \n " + item.getSource() + " \n\n " + "(Link download by uTorrent) \n " + item.getSource_2();
						}
					} else {
						msg = item.getSource();
					}

					msg = context.getString(R.string.suggest_download, msg);
					sendEmail(context, item.getName() + " - Link Download Dictionary", msg);
				}
			});
		}

		return v;
	}

	private static class ViewHolder {
		TextView nameLb;
		TextView sizeLb;
		Button downloadBtn;
		Button sendEmailBtn;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public DictionaryObject getItem(int index) {
		return list.get(index);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	private void sendEmail(Context context, String to, String subject, String body) {
		StringBuilder builder = new StringBuilder("mailto:" + Uri.encode(to));
		if (subject != null) {
			builder.append("?subject=" + Uri.encode(Uri.encode(subject)));
			if (body != null) {
				builder.append("&body=" + Uri.encode(Uri.encode(body)));
			}
		}
		String uri = builder.toString();
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
		context.startActivity(intent);
	}

	private void sendEmail(Context context, String subject, String body) {
		Intent intent = new Intent(Intent.ACTION_SENDTO);
		intent.setData(Uri.parse("mailto:"));
		// intent.putExtra(Intent.EXTRA_EMAIL , new String[] { me@somewhere.com
		// });
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, body);

		context.startActivity(Intent.createChooser(intent, "E-mail"));
	}
}
