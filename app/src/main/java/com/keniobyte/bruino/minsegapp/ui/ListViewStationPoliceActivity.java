package com.keniobyte.bruino.minsegapp.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.keniobyte.bruino.minsegapp.ui.adapters.ItemStationPoliceAdapter;
import com.keniobyte.bruino.minsegapp.model.PoliceStation;
import com.keniobyte.bruino.minsegapp.R;

/**
 * Police station's list with number, location, address and phone number.
 */

public class ListViewStationPoliceActivity extends ActionBarActivity {

    private PoliceStation[] data = new PoliceStation[]{
            new PoliceStation("Primera", "San Miguel de Tucumán", "San Martín 224", "4310480"),
            new PoliceStation("Segunda", "San Miguel de Tucumán", "Buenos Aires 479", "4248006"),
            new PoliceStation("Tercera", "San Miguel de Tucumán", "Lamadrid 2556", "4330439"),
            new PoliceStation("Cuarta", "San Miguel de Tucumán", "Diagonal Eugenio Mendez 369", "4248021"),
            new PoliceStation("Quinta", "San Miguel de Tucumán", "Muñecas 1658", "4273810"),
            new PoliceStation("Sexta", "San Miguel de Tucumán", "España 1715", "4320558"),
            new PoliceStation("Septima", "San Miguel de Tucumán", "Don Bosco 2601", "4330573"),
            new PoliceStation("Octava", "San Miguel de Tucumán", "Av. Poviña y Alsina", "4392541"),
            new PoliceStation("Novena", "San Miguel de Tucumán", "Av. Democracia y Ayacucho", "4295121"),
            new PoliceStation("Decima", "San Miguel de Tucumán", "Blas Parera N° 810", "4282887"),
            new PoliceStation("Once", "San Miguel de Tucumán", "Av Benjamin Aráoz 1095", "4310337"),
            new PoliceStation("Doce", "San Miguel de Tucumán", "Felix de Olazabal 1400", "4347787"),
            new PoliceStation("Trece", "San Miguel de Tucumán", "Magallanes 1390", "4366670"),
            new PoliceStation("Catorce", "San Miguel de Tucumán", "Manuel Estrada 2670", "4341256"),
            new PoliceStation("Comisaria de la Mujer", "San Miguel de Tucumán", "Buenos Aires 479", "4248069")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_police_station);

        setToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ItemStationPoliceAdapter adapterItemStationPolice = new ItemStationPoliceAdapter(this, R.layout.listitem_station, data);
        ListView listView = (ListView) findViewById(R.id.listViewStation);
        listView.setAdapter(adapterItemStationPolice);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle(getResources().getString(R.string.list_station));
        setSupportActionBar(toolbar);
    }

}
