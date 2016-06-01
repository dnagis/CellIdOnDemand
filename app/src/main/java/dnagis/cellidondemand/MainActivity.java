package dnagis.cellidondemand;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
        public void actionBouton(View view) {
            TextView monTextView = (TextView)findViewById(R.id.champ_texte);
            TextView monTextView2 = (TextView)findViewById(R.id.champ_texte2);
            ContentValues values = new ContentValues();





            //http://stackoverflow.com/questions/15576701/android-get-cellid-and-rss-for-base-station-and-neigboring-cells
            //CellInfoLte cellInfoLte = (CellInfoLte) cellinfo.get(0); //si c'est une cell LTE impossible de caster vers GSM
            //CellIdentityLte cellIdentity = cellInfoLte.getCellIdentity();
            //monTextView.setText(Integer.toString(cellinfo.size()));
            //http://www.programcreek.com/java-api-examples/index.php?api=android.telephony.CellInfo

            TelephonyManager telph= (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            //telph.getSimState();//ne détecte pas le mode avion: 5 tout le temps
            int mode_avion = Settings.System.getInt(this.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0); // =1 si mode avion (fait planter je sais pas quoi mais
            //je suppose getAllCellInfo() ou cellinfo.get(0)?

            int cellid = -1;

            if (mode_avion != 1) {
                List<CellInfo> cellinfo = telph.getAllCellInfo();
                CellInfo cell0 = cellinfo.get(0);
                monTextView.setText(cell0.toString());


                if (cell0 instanceof CellInfoGsm) {
                    cellid = ((CellInfoGsm) cell0).getCellIdentity().getCid();
                } else if (cell0 instanceof CellInfoCdma) {
                    cellid = ((CellInfoCdma) cell0).getCellIdentity().getBasestationId();
                } else if (cell0 instanceof CellInfoLte) {
                    cellid = ((CellInfoLte) cell0).getCellIdentity().getCi();
                } else if (cell0 instanceof CellInfoWcdma) {
                    cellid = ((CellInfoWcdma) cell0).getCellIdentity().getCid();
                }
            }

            monTextView2.setText(String.valueOf(cellid));


            long timestamp = System.currentTimeMillis();
            //Log.d("vincent_time",String.valueOf(timestamp));

       /* @Override
        public void onCellInfoChanged(List<CellInfo> cellInfo) {
            super.onCellInfoChanged(cellInfo);
            for(int i =0;i<cellInfo.size();i++){
                CellInfo m = cellInfo.get(i);
                if (m instanceof CellInfoLte){
                    CellInfoLte cellInfoLte=(CellInfoLte) m;
                    int pciNum = cellInfoLte.getCellIdentity().getPci();
                    Log.d("onCellInfoChanged", "CellInfoLte--" + m);
                    tv.setText("PCINUMBER: "+pciNum);
                }
            }

        Gson gson = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(cellinfo).getAsJsonArray();
        a_envoyer.telephony_data = myCustomArray.toString();*/

        }
    }

