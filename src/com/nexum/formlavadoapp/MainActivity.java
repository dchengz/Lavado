package com.nexum.formlavadoapp;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nexum.formlavadoapp.object.AppMethods;

public class MainActivity extends Activity {
	private int washType = -1;
	private EditText et_matricula, et_cliente, et_telf;
	private TextView tv_hora, tv_ticket, tv_amount;
	private static Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        h = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				if (msg.what==0) {
					Toast.makeText(getBaseContext(), msg.obj.toString(), Toast.LENGTH_LONG).show();
				}
				return false;
			}
		});
        et_matricula = (EditText) findViewById(R.id.et_matricula);
        et_cliente = (EditText) findViewById(R.id.et_cliente);
        et_telf = (EditText) findViewById(R.id.et_telf);
        tv_hora = (TextView) findViewById(R.id.tv_hora);
        tv_ticket = (TextView) findViewById(R.id.tv_ticket);
        tv_amount = (TextView) findViewById(R.id.tv_amount);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_print) {
        	return true;
        } else if (id == R.id.action_check) {
        	taskCons(id);
        	return true;
        } else if (id == R.id.action_getTicket) {
        	taskCons(id);
        	return true;
		}
        return super.onOptionsItemSelected(item);
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
    	RadioButton rb = (RadioButton) view;
        boolean checked = rb.isChecked();        
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rb_small:
                if (checked) {
                	washType = 0;
                	tv_amount.setText("10");
                }
                break;
            case R.id.rb_big:
                if (checked) {
                	washType = 1;
                	tv_amount.setText("15");
                }
                break;
            case R.id.rb_4x4:
                if (checked) {
                	washType = 2;
                	tv_amount.setText("20");
                }
                break;
        }
    }
    
    private void taskCons(int type){
		new AsyncTask<Integer, Void, String[]>() {
			@Override
			protected String[] doInBackground(Integer... params) {
				String data="", tipo="";
				try {
					if (params[0] == R.id.action_check) {
						tipo="check";
						data = AppMethods.check(et_matricula.getText().toString());
					} else if (params[0] == R.id.action_getTicket) {
						tipo="ticket";
						data = AppMethods.getTicket(et_matricula.getText().toString(), washType, tv_amount.getText().toString());
					}
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return new String[]{tipo,data};
			}
			@Override
			protected void onPostExecute(String[] result) {
				if (result[1].equals("ERROR")) {
					h.obtainMessage(0, "Error al enviar").sendToTarget();
				} else {
					try {
						JSONArray ja = new JSONArray(result[1]);
						JSONObject jo = ja.getJSONObject(0);
						if (result[0].equals("check")) {
							et_cliente.setText(jo.getString("cli"));
							et_telf.setText(jo.getString("telf"));
						} else if (result[0].equals("ticket")) {
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
							Calendar cal = Calendar.getInstance();
							cal.setTimeInMillis(Long.valueOf(jo.getString("date")));
							tv_hora.setText(sdf.format(cal.getTime()));
							tv_ticket.setText("N° Ticket: "+jo.getString("id"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}					
				}
			}			
		}.execute(type);
    }
}
