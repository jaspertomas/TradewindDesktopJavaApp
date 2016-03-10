package models;

import java.util.ArrayList;

import models.base.BaseSetting;

import org.json.JSONObject;

public class Setting extends BaseSetting{
	public Setting()
	{
	}
	public Setting(JSONObject values) {
		super(values);
	}
}