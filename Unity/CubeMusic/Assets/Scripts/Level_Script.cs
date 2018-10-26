using UnityEngine;
using System.Collections;
using UnityEngine.SceneManagement;

public class Level_Script : MonoBehaviour {

    void Start()
    {
     //   Debug.Log("LoadSceneB");
    }

    public void LoadB(int sceneANumber)
    {
        Debug.Log("sceneBuildIndex to load: " + sceneANumber);
        SceneManager.LoadScene(sceneANumber);
    }

    public void FecharJogo()
    {
        Application.Quit();
    }

}
