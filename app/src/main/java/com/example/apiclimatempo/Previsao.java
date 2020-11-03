package com.example.apiclimatempo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Previsao implements Serializable{

    private Integer _id;
    private String Data;
    private Integer _Icone ;
    private String _Clima;
    private String _Temperatura;
    private String _Cidade;

    public Previsao(){   }
    public Previsao(Integer _id,String Data,  String _Temperatura, String _Clima, Integer _Icone, String Cidade) {
        this.set_id(_id);
        this.set_Data(Data);
        this.set_Temperatura(_Temperatura);
        this.set_Clima(_Clima);
        this.set_Icone (_Icone);
        this.set_Cidade(Cidade);
    }


    public Previsao(String Data,  String _Temperatura, String _Clima, Integer _Icone, String Cidade) {
        this.set_Data(Data);
        this.set_Temperatura(_Temperatura);
        this.set_Clima(_Clima);
        this.set_Icone (_Icone);
        this.set_Cidade(Cidade);
    }
    public Integer get_id() {
        return _id;
    }
    public void set_id(Integer _id) {   this._id = _id;   }

    public String get_Data() {   return Data;  }

    public void set_Data(String Data) {   this.Data = Data;   }

    public String get_Temperatura() {
        return _Temperatura;
    }

    public void set_Temperatura(String _Temperatura) {
        this._Temperatura = _Temperatura;
    }


    public String get_Clima() {
        return _Clima;
    }

    public void set_Clima(String _Clima) {
        this._Clima = _Clima;
    }


    public String get_Cidade() {
        return _Cidade;
    }

    public void set_Cidade(String _Cidade) {
        this._Cidade = _Cidade;
    }

    public Integer get_Icone() {
        return _Icone;
    }

    public void set_Icone(Integer _Icone) {
        this._Icone = _Icone;
    }

    /*public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("Id", this._id);
            obj.put("Data", this.Data);
            obj.put("Temperatura", this._Temperatura);
            obj.put("Clima", this._Clima);
            obj.put("Icone", this._Icone);
            obj.put("Cidade", this._Cidade);
        } catch (JSONException e) {
            //trace("DefaultListItem.toString JSONException: "+e.getMessage());
        }
        return obj;
    }*/
}
