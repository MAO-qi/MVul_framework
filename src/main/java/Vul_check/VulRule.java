package Vul_check;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public abstract class VulRule {
     public abstract void check(int thread, List array, int len, int outtime ) throws IOException;
}
