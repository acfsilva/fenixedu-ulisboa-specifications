package org.fenixedu.ulisboa.specifications.domain.ects.tasks;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.fenixedu.ulisboa.specifications.domain.ects.GeneralPurposeGradingTable;

public class TestEctsGradeConversion extends CustomTask {

    @Override
    public void runTask() throws Exception {
        String[] control = new String[9];
        control[0] = "EDDDCCCBBBA"; // Uniform distro
        control[1] = "EDDCCBBBAAA"; // Chi-squared distro
        control[2] = "EDDCCBBBAAA"; // Normal distro
        control[3] = "EDDDCCBBAAA"; // Normal distro #2
        control[4] = "EEDDDDBBBAA"; // Tripolar distro
        control[5] = "EEEBBBBBBAA"; // Unbalanced tripolar distro
        control[6] = "EECCCCAAAAA"; // Bipolar distro
        control[7] = "EEEEEEEEAAA"; // Monopolar distro
        control[8] = "EEEEEEDCAAA"; // Clogged distro

        verify("Uniform distro", generateSampleUniform(), control[0]);
        verify("Chi-squared distro", generateSampleChiSq(), control[1]);
        verify("Normal distro", generateSampleNormal(), control[2]);
        verify("Normal distro #2", generateSampleNormal2(), control[3]);
        verify("Tripolar distro", generateSampleTripolar(), control[4]);
        verify("Unbalanced tripolar distro", generateSampleTripolarUnbalanced(), control[5]);
        verify("Bipolar distro", generateSampleBipolar(), control[6]);
        verify("Monopolar distro", generateSampleMonopolar(), control[7]);
        verify("Clogged distro", generateSampleClogged(), control[8]);
    }

    public void verify(String testTitle, List<BigDecimal> input, String expectedOutput) {
        GeneralPurposeGradingTable testingTable = new GeneralPurposeGradingTable(input);
        String result = testingTable.printScale();
        if (result.equals(new StringBuilder(expectedOutput).toString())) {
            taskLog("✓ " + testTitle + ": PASSED");
        } else {
            taskLog("  " + testTitle + ": FAILED\n  Expected: " + expectedOutput + "\n  Obtained: " + result + "\n");
        }
        testingTable.delete();
    }

    public List<BigDecimal> generateSampleUniform() {
        List<BigDecimal> sample = new ArrayList<BigDecimal>();
        for (int i = 0; i < 100; i++) {
            if (i < 10) {
                sample.add(new BigDecimal("10.0"));
            } else if (i < 19) {
                sample.add(new BigDecimal("11.0"));
            } else if (i < 28) {
                sample.add(new BigDecimal("12.0"));
            } else if (i < 37) {
                sample.add(new BigDecimal("13.0"));
            } else if (i < 46) {
                sample.add(new BigDecimal("14.0"));
            } else if (i < 55) {
                sample.add(new BigDecimal("15.0"));
            } else if (i < 64) {
                sample.add(new BigDecimal("16.0"));
            } else if (i < 73) {
                sample.add(new BigDecimal("17.0"));
            } else if (i < 82) {
                sample.add(new BigDecimal("18.0"));
            } else if (i < 91) {
                sample.add(new BigDecimal("19.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("20.0"));
            }
        }
        return sample;
    }

    public List<BigDecimal> generateSampleChiSq() {
        List<BigDecimal> sample = new ArrayList<BigDecimal>();
        for (int i = 0; i < 100; i++) {
            if (i < 10) {
                sample.add(new BigDecimal("10.0"));
            } else if (i < 22) {
                sample.add(new BigDecimal("11.0"));
            } else if (i < 35) {
                sample.add(new BigDecimal("12.0"));
            } else if (i < 46) {
                sample.add(new BigDecimal("13.0"));
            } else if (i < 69) {
                sample.add(new BigDecimal("14.0"));
            } else if (i < 81) {
                sample.add(new BigDecimal("15.0"));
            } else if (i < 86) {
                sample.add(new BigDecimal("16.0"));
            } else if (i < 91) {
                sample.add(new BigDecimal("17.0"));
            } else if (i < 95) {
                sample.add(new BigDecimal("18.0"));
            } else if (i < 98) {
                sample.add(new BigDecimal("19.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("20.0"));
            }
        }
        return sample;
    }

    public List<BigDecimal> generateSampleNormal() {
        List<BigDecimal> sample = new ArrayList<BigDecimal>();
        for (int i = 0; i < 100; i++) {
            if (i < 8) {
                sample.add(new BigDecimal("10.0"));
            } else if (i < 26) {
                sample.add(new BigDecimal("11.0"));
            } else if (i < 32) {
                sample.add(new BigDecimal("12.0"));
            } else if (i < 45) {
                sample.add(new BigDecimal("13.0"));
            } else if (i < 64) {
                sample.add(new BigDecimal("14.0"));
            } else if (i < 81) {
                sample.add(new BigDecimal("15.0"));
            } else if (i < 86) {
                sample.add(new BigDecimal("16.0"));
            } else if (i < 91) {
                sample.add(new BigDecimal("17.0"));
            } else if (i < 95) {
                sample.add(new BigDecimal("18.0"));
            } else if (i < 98) {
                sample.add(new BigDecimal("19.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("20.0"));
            }
        }
        return sample;
    }

    public List<BigDecimal> generateSampleNormal2() {
        List<BigDecimal> sample = new ArrayList<BigDecimal>();
        for (int i = 0; i < 100; i++) {
            if (i < 7) {
                sample.add(new BigDecimal("10.0"));
            } else if (i < 14) {
                sample.add(new BigDecimal("11.0"));
            } else if (i < 21) {
                sample.add(new BigDecimal("12.0"));
            } else if (i < 31) {
                sample.add(new BigDecimal("13.0"));
            } else if (i < 56) {
                sample.add(new BigDecimal("14.0"));
            } else if (i < 71) {
                sample.add(new BigDecimal("15.0"));
            } else if (i < 83) {
                sample.add(new BigDecimal("16.0"));
            } else if (i < 88) {
                sample.add(new BigDecimal("17.0"));
            } else if (i < 95) {
                sample.add(new BigDecimal("18.0"));
            } else if (i < 98) {
                sample.add(new BigDecimal("19.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("20.0"));
            }
        }
        return sample;
    }

    public List<BigDecimal> generateSampleTripolar() {
        List<BigDecimal> sample = new ArrayList<BigDecimal>();
        for (int i = 0; i < 100; i++) {
            if (i < 0) {
                sample.add(new BigDecimal("10.0"));
            } else if (i < 0) {
                sample.add(new BigDecimal("11.0"));
            } else if (i < 34) {
                sample.add(new BigDecimal("12.0"));
            } else if (i < 34) {
                sample.add(new BigDecimal("13.0"));
            } else if (i < 34) {
                sample.add(new BigDecimal("14.0"));
            } else if (i < 34) {
                sample.add(new BigDecimal("15.0"));
            } else if (i < 67) {
                sample.add(new BigDecimal("16.0"));
            } else if (i < 67) {
                sample.add(new BigDecimal("17.0"));
            } else if (i < 67) {
                sample.add(new BigDecimal("18.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("19.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("20.0"));
            }
        }
        return sample;
    }

    public List<BigDecimal> generateSampleTripolarUnbalanced() {
        List<BigDecimal> sample = new ArrayList<BigDecimal>();
        for (int i = 0; i < 100; i++) {
            if (i < 0) {
                sample.add(new BigDecimal("10.0"));
            } else if (i < 0) {
                sample.add(new BigDecimal("11.0"));
            } else if (i < 0) {
                sample.add(new BigDecimal("12.0"));
            } else if (i < 74) {
                sample.add(new BigDecimal("13.0"));
            } else if (i < 74) {
                sample.add(new BigDecimal("14.0"));
            } else if (i < 74) {
                sample.add(new BigDecimal("15.0"));
            } else if (i < 75) {
                sample.add(new BigDecimal("16.0"));
            } else if (i < 75) {
                sample.add(new BigDecimal("17.0"));
            } else if (i < 75) {
                sample.add(new BigDecimal("18.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("19.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("20.0"));
            }
        }
        return sample;
    }

    public List<BigDecimal> generateSampleBipolar() {
        List<BigDecimal> sample = new ArrayList<BigDecimal>();
        for (int i = 0; i < 100; i++) {
            if (i < 0) {
                sample.add(new BigDecimal("10.0"));
            } else if (i < 0) {
                sample.add(new BigDecimal("11.0"));
            } else if (i < 50) {
                sample.add(new BigDecimal("12.0"));
            } else if (i < 50) {
                sample.add(new BigDecimal("13.0"));
            } else if (i < 50) {
                sample.add(new BigDecimal("14.0"));
            } else if (i < 50) {
                sample.add(new BigDecimal("15.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("16.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("17.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("18.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("19.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("20.0"));
            }
        }
        return sample;
    }

    public List<BigDecimal> generateSampleMonopolar() {
        List<BigDecimal> sample = new ArrayList<BigDecimal>();
        for (int i = 0; i < 100; i++) {
            if (i < 0) {
                sample.add(new BigDecimal("10.0"));
            } else if (i < 0) {
                sample.add(new BigDecimal("11.0"));
            } else if (i < 0) {
                sample.add(new BigDecimal("12.0"));
            } else if (i < 0) {
                sample.add(new BigDecimal("13.0"));
            } else if (i < 0) {
                sample.add(new BigDecimal("14.0"));
            } else if (i < 0) {
                sample.add(new BigDecimal("15.0"));
            } else if (i < 0) {
                sample.add(new BigDecimal("16.0"));
            } else if (i < 0) {
                sample.add(new BigDecimal("17.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("18.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("19.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("20.0"));
            }
        }
        return sample;
    }

    public List<BigDecimal> generateSampleClogged() {
        List<BigDecimal> sample = new ArrayList<BigDecimal>();
        for (int i = 0; i < 100; i++) {
            if (i < 0) {
                sample.add(new BigDecimal("10.0"));
            } else if (i < 0) {
                sample.add(new BigDecimal("11.0"));
            } else if (i < 0) {
                sample.add(new BigDecimal("12.0"));
            } else if (i < 0) {
                sample.add(new BigDecimal("13.0"));
            } else if (i < 0) {
                sample.add(new BigDecimal("14.0"));
            } else if (i < 5) {
                sample.add(new BigDecimal("15.0"));
            } else if (i < 24) {
                sample.add(new BigDecimal("16.0"));
            } else if (i < 64) {
                sample.add(new BigDecimal("17.0"));
            } else if (i < 93) {
                sample.add(new BigDecimal("18.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("19.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("20.0"));
            }
        }
        return sample;
    }
}