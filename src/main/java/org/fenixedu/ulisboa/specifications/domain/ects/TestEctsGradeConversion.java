package org.fenixedu.ulisboa.specifications.domain.ects;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.fenixedu.bennu.scheduler.custom.CustomTask;

public class TestEctsGradeConversion extends CustomTask {

    @Override
    public void runTask() throws Exception {
        List<BigDecimal> sample = generateSampleUniform();
        generateTableData(sample);

        sample.clear();
        sample = generateSampleChi2();
        generateTableData(sample);

        sample.clear();
        sample = generateSampleNormal();
        generateTableData(sample);

        sample.clear();
        sample = generateSampleNormal2();
        generateTableData(sample);

        sample.clear();
        sample = generateSampleTripolar();
        generateTableData(sample);

        sample.clear();
        sample = generateSampleTripolarUnbalanced();
        generateTableData(sample);

        sample.clear();
        sample = generateSampleBipolar();
        generateTableData(sample);

        sample.clear();
        sample = generateSampleMonopolar();
        generateTableData(sample);

    }

    public List<BigDecimal> generateSampleUniform() {
        List<BigDecimal> sample = new ArrayList<BigDecimal>();
        for (int i = 0; i < 100; i++) {
            if (i < 9) {
                sample.add(new BigDecimal("20.0"));
            } else if (i < 18) {
                sample.add(new BigDecimal("19.0"));
            } else if (i < 27) {
                sample.add(new BigDecimal("18.0"));
            } else if (i < 36) {
                sample.add(new BigDecimal("17.0"));
            } else if (i < 45) {
                sample.add(new BigDecimal("16.0"));
            } else if (i < 54) {
                sample.add(new BigDecimal("15.0"));
            } else if (i < 63) {
                sample.add(new BigDecimal("14.0"));
            } else if (i < 72) {
                sample.add(new BigDecimal("13.0"));
            } else if (i < 81) {
                sample.add(new BigDecimal("12.0"));
            } else if (i < 90) {
                sample.add(new BigDecimal("11.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("10.0"));
            }
        }
        return sample;
    }

    public List<BigDecimal> generateSampleChi2() {
        List<BigDecimal> sample = new ArrayList<BigDecimal>();
        for (int i = 0; i < 100; i++) {
            if (i < 2) {
                sample.add(new BigDecimal("20.0"));
            } else if (i < 5) {
                sample.add(new BigDecimal("19.0"));
            } else if (i < 9) {
                sample.add(new BigDecimal("18.0"));
            } else if (i < 14) {
                sample.add(new BigDecimal("17.0"));
            } else if (i < 19) {
                sample.add(new BigDecimal("16.0"));
            } else if (i < 31) {
                sample.add(new BigDecimal("15.0"));
            } else if (i < 54) {
                sample.add(new BigDecimal("14.0"));
            } else if (i < 65) {
                sample.add(new BigDecimal("13.0"));
            } else if (i < 78) {
                sample.add(new BigDecimal("12.0"));
            } else if (i < 90) {
                sample.add(new BigDecimal("11.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("10.0"));
            }
        }
        return sample;
    }

    public List<BigDecimal> generateSampleNormal() {
        List<BigDecimal> sample = new ArrayList<BigDecimal>();
        for (int i = 0; i < 100; i++) {
            if (i < 2) {
                sample.add(new BigDecimal("20.0"));
            } else if (i < 5) {
                sample.add(new BigDecimal("19.0"));
            } else if (i < 9) {
                sample.add(new BigDecimal("18.0"));
            } else if (i < 14) {
                sample.add(new BigDecimal("17.0"));
            } else if (i < 19) {
                sample.add(new BigDecimal("16.0"));
            } else if (i < 36) {
                sample.add(new BigDecimal("15.0"));
            } else if (i < 55) {
                sample.add(new BigDecimal("14.0"));
            } else if (i < 68) {
                sample.add(new BigDecimal("13.0"));
            } else if (i < 74) {
                sample.add(new BigDecimal("12.0"));
            } else if (i < 92) {
                sample.add(new BigDecimal("11.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("10.0"));
            }
        }
        return sample;
    }

    public List<BigDecimal> generateSampleNormal2() {
        List<BigDecimal> sample = new ArrayList<BigDecimal>();
        for (int i = 0; i < 100; i++) {
            if (i < 2) {
                sample.add(new BigDecimal("20.0"));
            } else if (i < 5) {
                sample.add(new BigDecimal("19.0"));
            } else if (i < 12) {
                sample.add(new BigDecimal("18.0"));
            } else if (i < 17) {
                sample.add(new BigDecimal("17.0"));
            } else if (i < 29) {
                sample.add(new BigDecimal("16.0"));
            } else if (i < 44) {
                sample.add(new BigDecimal("15.0"));
            } else if (i < 69) {
                sample.add(new BigDecimal("14.0"));
            } else if (i < 79) {
                sample.add(new BigDecimal("13.0"));
            } else if (i < 86) {
                sample.add(new BigDecimal("12.0"));
            } else if (i < 93) {
                sample.add(new BigDecimal("11.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("10.0"));
            }
        }
        return sample;
    }

    public List<BigDecimal> generateSampleTripolar() {
        List<BigDecimal> sample = new ArrayList<BigDecimal>();
        for (int i = 0; i < 100; i++) {
            if (i < 0) {
                sample.add(new BigDecimal("20.0"));
            } else if (i < 0) {
                sample.add(new BigDecimal("19.0"));
            } else if (i < 33) {
                sample.add(new BigDecimal("18.0"));
            } else if (i < 33) {
                sample.add(new BigDecimal("17.0"));
            } else if (i < 33) {
                sample.add(new BigDecimal("16.0"));
            } else if (i < 33) {
                sample.add(new BigDecimal("15.0"));
            } else if (i < 66) {
                sample.add(new BigDecimal("14.0"));
            } else if (i < 66) {
                sample.add(new BigDecimal("13.0"));
            } else if (i < 66) {
                sample.add(new BigDecimal("12.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("11.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("10.0"));
            }
        }
        return sample;
    }

    public List<BigDecimal> generateSampleTripolarUnbalanced() {
        List<BigDecimal> sample = new ArrayList<BigDecimal>();
        for (int i = 0; i < 100; i++) {
            if (i < 0) {
                sample.add(new BigDecimal("20.0"));
            } else if (i < 0) {
                sample.add(new BigDecimal("19.0"));
            } else if (i < 74) {
                sample.add(new BigDecimal("18.0"));
            } else if (i < 74) {
                sample.add(new BigDecimal("17.0"));
            } else if (i < 74) {
                sample.add(new BigDecimal("16.0"));
            } else if (i < 74) {
                sample.add(new BigDecimal("15.0"));
            } else if (i < 75) {
                sample.add(new BigDecimal("14.0"));
            } else if (i < 75) {
                sample.add(new BigDecimal("13.0"));
            } else if (i < 75) {
                sample.add(new BigDecimal("12.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("11.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("10.0"));
            }
        }
        return sample;
    }

    public List<BigDecimal> generateSampleBipolar() {
        List<BigDecimal> sample = new ArrayList<BigDecimal>();
        for (int i = 0; i < 100; i++) {
            if (i < 0) {
                sample.add(new BigDecimal("20.0"));
            } else if (i < 0) {
                sample.add(new BigDecimal("19.0"));
            } else if (i < 50) {
                sample.add(new BigDecimal("18.0"));
            } else if (i < 50) {
                sample.add(new BigDecimal("17.0"));
            } else if (i < 50) {
                sample.add(new BigDecimal("16.0"));
            } else if (i < 50) {
                sample.add(new BigDecimal("15.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("14.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("13.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("12.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("11.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("10.0"));
            }
        }
        return sample;
    }

    public List<BigDecimal> generateSampleMonopolar() {
        List<BigDecimal> sample = new ArrayList<BigDecimal>();
        for (int i = 0; i < 100; i++) {
            if (i < 0) {
                sample.add(new BigDecimal("20.0"));
            } else if (i < 0) {
                sample.add(new BigDecimal("19.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("18.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("17.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("16.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("15.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("14.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("13.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("12.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("11.0"));
            } else if (i < 100) {
                sample.add(new BigDecimal("10.0"));
            }
        }
        return sample;
    }

    public void generateTableData(List<BigDecimal> sample) {
        Map<BigDecimal, Integer> gradeDistro = new LinkedHashMap<BigDecimal, Integer>();
        Map<BigDecimal, BigDecimal> heapedGradeDistro = new LinkedHashMap<BigDecimal, BigDecimal>();
        BigDecimal sampleSize = new BigDecimal(sample.size());
        gradeDistro.put(new BigDecimal("20.0"), 0);
        gradeDistro.put(new BigDecimal("19.0"), 0);
        gradeDistro.put(new BigDecimal("18.0"), 0);
        gradeDistro.put(new BigDecimal("17.0"), 0);
        gradeDistro.put(new BigDecimal("16.0"), 0);
        gradeDistro.put(new BigDecimal("15.0"), 0);
        gradeDistro.put(new BigDecimal("14.0"), 0);
        gradeDistro.put(new BigDecimal("13.0"), 0);
        gradeDistro.put(new BigDecimal("12.0"), 0);
        gradeDistro.put(new BigDecimal("11.0"), 0);
        gradeDistro.put(new BigDecimal("10.0"), 0);

        // 1. Grades distributions
        for (BigDecimal grade : sample) {
            gradeDistro.put(grade, (gradeDistro.get(grade) + 1));
        }

        // 2. Heaped grades distribution
        BigDecimal heap = BigDecimal.ZERO;
        for (Entry<BigDecimal, Integer> step : gradeDistro.entrySet()) {
            BigDecimal grade = step.getKey();
            BigDecimal count = new BigDecimal(step.getValue());
            BigDecimal share = count.divide(sampleSize, 5, BigDecimal.ROUND_HALF_EVEN);
            heap = heap.add(share);
            heapedGradeDistro.put(grade, heap);
        }

        // 3. Initial table fill ABCDEEEEEEE
        Map<BigDecimal, String> tableMap = new LinkedHashMap<BigDecimal, String>();
        tableMap.put(new BigDecimal("20.0"), "A");
        tableMap.put(new BigDecimal("19.0"), "B");
        tableMap.put(new BigDecimal("18.0"), "C");
        tableMap.put(new BigDecimal("17.0"), "D");
        tableMap.put(new BigDecimal("16.0"), "E");
        tableMap.put(new BigDecimal("15.0"), "E");
        tableMap.put(new BigDecimal("14.0"), "E");
        tableMap.put(new BigDecimal("13.0"), "E");
        tableMap.put(new BigDecimal("12.0"), "E");
        tableMap.put(new BigDecimal("11.0"), "E");
        tableMap.put(new BigDecimal("10.0"), "E");

        // 4. Shift according to distribution
        int gradesSize = tableMap.keySet().size();
        int ectsGradesSize = tableMap.values().stream().distinct().mapToInt(s -> 1).sum();
        int gradesCnt = 0;
        for (Entry<BigDecimal, BigDecimal> step : heapedGradeDistro.entrySet()) {
            BigDecimal grade = step.getKey();
            BigDecimal heapedShare = step.getValue();
            String ectsGrade = getEctsGrade(heapedShare);
            String grantedValue = tableMap.get(grade);
            gradesCnt++;

            //taskLog(grade.toPlainString() + "\t" + ectsGrade + "\t" + grantedValue);
            if (isStagnant(grade, heapedGradeDistro)) {
                if (shouldIncrement(grade, grantedValue, heapedGradeDistro, tableMap)) {
                    switch (grantedValue) {
                    case "A":
                        tableMap.put(grade, "B");
                        break;
                    case "B":
                        tableMap.put(grade, "C");
                        break;
                    case "C":
                        tableMap.put(grade, "D");
                        break;
                    case "D":
                        tableMap.put(grade, "E");
                        break;
                    default:
                        break;
                    }
                    ectsGradesSize--;
                } else if (shouldNormalize(grade, grantedValue, heapedGradeDistro, tableMap)) {
                    tableMap.put(grade, getPrevGrade(grade, tableMap));
                }

            } else {
                // Inflate
                if (ectsGrade.compareTo(grantedValue) < 0) {
                    if (((gradesSize - gradesCnt) >= ectsGradesSize)) {
                        boolean found = false;
                        String updateValue = "";
                        for (Entry<BigDecimal, String> entry : tableMap.entrySet()) {
                            if (entry.getKey().equals(grade)) {
                                found = true;
                                updateValue = entry.getValue();
                                entry.setValue(ectsGrade);
                            } else if (found) {
                                String passValue = entry.getValue();
                                entry.setValue(updateValue);
                                updateValue = passValue;
                            }
                        }
                    }
                } else {
                    ectsGradesSize--;
                }
            }
        }

        // 5. Populate the table with final values
        String grades = "";
        String ectsGrades = "";
        for (Entry<BigDecimal, String> entry : tableMap.entrySet()) {
            grades += entry.getKey().toPlainString() + "\t";
            ectsGrades += entry.getValue() + "\t";
        }
        taskLog(grades.trim());
        taskLog(ectsGrades.trim());
    }

    private String getEctsGrade(BigDecimal heapedDistro) {
        for (Entry<String, BigDecimal> interval : GradingTableSettings.getEctsAccumulativeDistro().entrySet()) {
            if (heapedDistro.compareTo(interval.getValue()) < 1) {
                return interval.getKey();
            }
        }
        return null;
    }

    private boolean shouldIncrement(BigDecimal grade, String ectsGrade, Map<BigDecimal, BigDecimal> heapedGradeDistro,
            Map<BigDecimal, String> tableMap) {
        boolean isStagnant = isStagnant(grade, heapedGradeDistro);
        BigDecimal nextHeap = nextIncrement(grade, heapedGradeDistro);
        String nextEctsGrade = nextHeap != null ? getEctsGrade(nextHeap) : ectsGrade;
//        taskLog("I\t" + grade.toPlainString() + "\t" + String.valueOf(isStagnant) + "\t"
//                + String.valueOf(hasIncrementedEcts(grade, tableMap)) + "\t" + ectsGrade + "\t" + nextEctsGrade);

        return isStagnant && !hasIncrementedEcts(grade, tableMap) && (compareEctsGrade(ectsGrade, nextEctsGrade) < 0);
    }

    private boolean shouldNormalize(BigDecimal grade, String ectsGrade, Map<BigDecimal, BigDecimal> heapedGradeDistro,
            Map<BigDecimal, String> tableMap) {
        boolean isStagnant = isStagnant(grade, heapedGradeDistro);
        BigDecimal nextHeap = nextIncrement(grade, heapedGradeDistro);
        String nextEctsGrade = nextHeap != null ? getEctsGrade(nextHeap) : ectsGrade;
//        taskLog("N\t" + grade.toPlainString() + "\t" + String.valueOf(isStagnant) + "\t"
//                + String.valueOf(hasIncrementedEcts(grade, tableMap)) + "\t" + ectsGrade + "\t" + nextEctsGrade);

        return isStagnant && (compareEctsGrade(ectsGrade, nextEctsGrade) > 0);
    }

    private boolean isStagnant(BigDecimal grade, Map<BigDecimal, BigDecimal> heapedGradeDistro) {
        BigDecimal prevHeap = null;
        for (Entry<BigDecimal, BigDecimal> step : heapedGradeDistro.entrySet()) {
            if (step.getKey().equals(grade)) {
                return prevHeap != null && prevHeap.equals(step.getValue());
            }
            prevHeap = step.getValue();
        }
        return false;
    }

    private BigDecimal nextIncrement(BigDecimal grade, Map<BigDecimal, BigDecimal> heapedGradeDistro) {
        BigDecimal prevHeap = null;
        for (Entry<BigDecimal, BigDecimal> step : heapedGradeDistro.entrySet()) {
            if (step.getKey().equals(grade)) {
                prevHeap = step.getValue();
            }
            if (prevHeap != null && prevHeap.compareTo(step.getValue()) < 0) {
                return step.getValue();
            }
        }
        return null;
    }

    private boolean hasIncrementedEcts(BigDecimal grade, Map<BigDecimal, String> tableMap) {
        String prevEctsGrade = null;
        for (Entry<BigDecimal, String> step : tableMap.entrySet()) {
            if (step.getKey().equals(grade)) {
                return prevEctsGrade != null && !prevEctsGrade.equals(step.getValue());
            }
            prevEctsGrade = step.getValue();
        }
        return false;
    }

    private String getPrevGrade(BigDecimal grade, Map<BigDecimal, String> tableMap) {
        String prevEctsGrade = null;
        for (Entry<BigDecimal, String> step : tableMap.entrySet()) {
            if (step.getKey().equals(grade)) {
                return prevEctsGrade;
            }
            prevEctsGrade = step.getValue();
        }
        return prevEctsGrade;
    }

    private int compareEctsGrade(String ects0, String ects1) {
        return ects0.charAt(0) - ects1.charAt(0);
    }

}