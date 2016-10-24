// ----------------------------------------------------------------------------
//  JOgmaNeo
//  Copyright(c) 2016 Ogma Intelligent Systems Corp. All rights reserved.
//
//  This copy of JOgmaNeo is licensed to you under the terms described
//  in the JOGMANEO_LICENSE.md file included in this distribution.
// ----------------------------------------------------------------------------

import java.io.File;
import com.ogmacorp.ogmaneo.*;

public class Example {

    static private ComputeSystemInterface _csi = new ComputeSystemInterface();
    static private ComputeProgramInterface _cpi = new ComputeProgramInterface();

    static private boolean serializationEnabled = false;

    public static void main(String[] args) {
        int numSimSteps = 100;

        if (args.length > 0) {
            try {
                numSimSteps = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Argument" + args[0] + " must be an integer.");
                System.exit(1);
            }
        }

        _csi.create(ComputeSystem.DeviceType._cpu);
        _cpi.loadMainKernel(_csi);

        // Input size (width and height)
        int w = 16;
        int h = 16;

        LayerDescs[] layers = new LayerDescs[2];
        vectorld layer_descs = new vectorld();

        for (LayerDescs layer : layers) {
            layer = new LayerDescs();
            layer.set_width(256);
            layer.set_height(256);
            layer_descs.add(layer);
        }

        Hierarchy hierarchy = new Hierarchy(_csi.get(), _cpi.get(), w, h, layer_descs, -0.01f, 0.01f, 1337);

        if (serializationEnabled) {
            File f = new File("Example.opr");
            if (f.exists() && !f.isDirectory()) {
                System.out.println("Loading hierarchy from Example.opr");
                hierarchy.load(_csi.get(), _cpi.get(), "Example.opr");
            }
        }

        vectorf input = new vectorf();
        for (int x = 0; x < (w*h); x++) {
            input.add(x);
        }

        vectorf prediction = null;
        for (int i = 0; i < numSimSteps; i++) {
            hierarchy.simStep(input, true);

            prediction = hierarchy.getPrediction();

            System.out.print(".");
        }
        System.out.println();

        if (numSimSteps > 0) {
            System.out.print("Input      :");
            for (int x = 0; x < (w*h); x++) {
                System.out.printf(" %.2f", input.get(x));
            }
            System.out.println();

            System.out.print("Prediction :");
            for (int x = 0; x < (w*h); x++) {
                System.out.printf(" %.2f", prediction.get(x));
            }
            System.out.println();

            if (serializationEnabled) {
                System.out.println("Saving hierarchy to Example.opr");
                hierarchy.save(_csi.get(), "Example.opr");
            }
        }
    }
}
