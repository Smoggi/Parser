package m.parser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import org.htmlcleaner.TagNode;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button button = (Button)findViewById(R.id.parse);
        button.setOnClickListener(myListener);
    }
    private ProgressDialog pd;
    private View.OnClickListener myListener = new View.OnClickListener() {

        public void onClick(View v) {
            pd = ProgressDialog.show(m.parser.MainActivity.this,"Working...", "request to server",true,false);
            new ParseSite().execute("http://live.goodline.info");
        }
    };
    private class ParseSite extends AsyncTask<String, Void, List<String>> {
        protected List<String> doInBackground(String... arg) {
            List<String> output = new ArrayList<String>();
            try {
                HtmlHelper hh = new HtmlHelper(new URL(arg[0]));
                List<TagNode> links = hh.getLinksByClass("topic-title word-wrap");

                for (Iterator<TagNode> iterator = links.iterator(); iterator.hasNext(); ) {
                    TagNode divElement = (TagNode) iterator.next();
                    output.add(divElement.getText().toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return output;
        }

        protected void onPostExecute(List<String> output) {
            pd.dismiss();
            ListView listview = (ListView) findViewById(R.id.listViewData);
            listview.setAdapter(new ArrayAdapter<String>(m.parser.MainActivity.this,
                    android.R.layout.simple_list_item_1, output));
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
