using System.Collections;
using UnityEngine;
using UnityEngine.UI;

public class CountDown : MonoBehaviour {
	public Text displayContagem;

	public float contagem = 3.0f;

	// Use this for initialization
	void Start () {

	}

	// Update is called once per frame
	void Update ()
	{
		if (contagem > 0.0f)
		{
			contagem -= Time.deltaTime;
			displayContagem.text = contagem.ToString("F1");
		}
		else
		{
			displayContagem.text = "Fim";
		}

	}
}
