package dnagis.cellidondemand;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.IntegerRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;
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
            TextView allcellinfoTextView = (TextView)findViewById(R.id.text_cellinfo);
            TextView cellidTextView = (TextView)findViewById(R.id.text_cellid);
            TextView cellinfosizeTextView = (TextView)findViewById(R.id.text_n_cells);
            TextView lacTextView = (TextView)findViewById(R.id.text_lac);
            TextView mccTextView = (TextView)findViewById(R.id.text_mcc);
            TextView mncTextView = (TextView)findViewById(R.id.text_mnc);
            TextView radioTextView = (TextView)findViewById(R.id.text_radio);

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
            int cellid = -1; //par défault
            int lac = -1;
            int mnc = -1;
            int mcc = -1;
            String radio = "unknown";


            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION); //depuis API23 les permissions au runtime... sigh...
            if (permissionCheck == PackageManager.PERMISSION_DENIED)
                cellid = -18;



            if ((mode_avion != 1) && (permissionCheck == PackageManager.PERMISSION_GRANTED)) {
                //http://www.programcreek.com/java-api-examples/index.php?api=android.telephony.CellInfo
                    List<CellInfo> cellinfo = telph.getAllCellInfo();
                //dans le métro quand cartes sims activées mais aucun signal ça plante... je suspecte soit cellinfo null (cellinfo != null)-pas suffisant soit size=0
                if (cellinfo.size() > 0) {
                    cellinfosizeTextView.setText(String.valueOf(cellinfo.size()));
                    CellInfo cell0 = cellinfo.get(0);
                    allcellinfoTextView.setText(cell0.toString());

                    if (cell0 instanceof CellInfoGsm) {
                        cellid = ((CellInfoGsm) cell0).getCellIdentity().getCid();
                        lac = ((CellInfoGsm) cell0).getCellIdentity().getLac();
                        mnc = ((CellInfoGsm) cell0).getCellIdentity().getMnc();
                        mcc = ((CellInfoGsm) cell0).getCellIdentity().getMcc();
                        radio = "GSM";
                    } else if (cell0 instanceof CellInfoCdma) { //2g ??
                        cellid = ((CellInfoCdma) cell0).getCellIdentity().getBasestationId();
                        radio = "CDMA";
                    } else if (cell0 instanceof CellInfoLte) { //4G??
                        cellid = ((CellInfoLte) cell0).getCellIdentity().getCi();
                        mnc = ((CellInfoLte) cell0).getCellIdentity().getMnc();
                        mcc = ((CellInfoLte) cell0).getCellIdentity().getMcc();
                        lac = ((CellInfoLte) cell0).getCellIdentity().getTac();
                        //String lacz = cell0.toString();
                        radio = "LTE";
                        //Log.d("vincent",String.valueOf(cell0.toString()));
                        //Log.d("vincent", Integer.toString(j));

                    } else if (cell0 instanceof CellInfoWcdma) { //3G? UMTS?
                        cellid = ((CellInfoWcdma) cell0).getCellIdentity().getCid();
                        lac = ((CellInfoWcdma) cell0).getCellIdentity().getLac();
                        mnc = ((CellInfoWcdma) cell0).getCellIdentity().getMnc();
                        mcc = ((CellInfoWcdma) cell0).getCellIdentity().getMcc();
                        radio = "UMTS";
                    }

                } else {
                    cellid = 0; //code pour se rappeler que cellid est vide (genre métro)
                }


            }

            cellidTextView.setText(String.valueOf(cellid));
            lacTextView.setText(String.valueOf(lac));
            mccTextView.setText(String.valueOf(mcc));
            mncTextView.setText(String.valueOf(mnc));
            radioTextView.setText(String.valueOf(radio));


            //long timestamp = System.currentTimeMillis();
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


