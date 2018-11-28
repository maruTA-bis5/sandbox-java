import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.bbreak.excella.reports.exporter.ExcelExporter;
import org.bbreak.excella.reports.model.ParamInfo;
import org.bbreak.excella.reports.model.ReportBook;
import org.bbreak.excella.reports.model.ReportSheet;
import org.bbreak.excella.reports.processor.ReportProcessor;
import org.bbreak.excella.reports.tag.BlockRowRepeatParamParser;
import org.bbreak.excella.reports.tag.SingleParamParser;
import org.junit.Test;

public class MergedRegionBRTest {

    private static final String TEMPLATE = "MergedRegionBR.xlsx";

    @Test
    public void パターン1() throws Exception {
        ReportBook book = new ReportBook( getTemplateUrl(), "Pattern1output", ExcelExporter.FORMAT_TYPE);
        ReportSheet sheet = createReportSheet( "Pattern1");
        book.addReportSheet( sheet);

        ReportProcessor processor = new ReportProcessor();
        processor.process( book);
    }

    private ReportSheet createReportSheet( String sheetName) {
        ReportSheet sheet = new ReportSheet( sheetName);

        sheet.addParam( SingleParamParser.DEFAULT_TAG, "A", "Adata");
        List<ParamInfo> repeatData = new ArrayList<>();
        for ( int i = 1; i <= 3; i++) {
            ParamInfo row = new ParamInfo();
            row.addParam( SingleParamParser.DEFAULT_TAG, "rownum", String.valueOf( i));
            repeatData.add( row);
        }
        sheet.addParam( BlockRowRepeatParamParser.DEFAULT_TAG, "B", repeatData.toArray());
        sheet.addParam( SingleParamParser.DEFAULT_TAG, "C", "Cdata");

        return sheet;
    }

    private URL getTemplateUrl() {
        return getClass().getResource( TEMPLATE);
    }

    @Test
    public void パターン2() throws Exception {
        ReportBook book = new ReportBook( getTemplateUrl(), "Pattern2output", ExcelExporter.FORMAT_TYPE);
        ReportSheet sheet = createReportSheet( "Pattern2");
        book.addReportSheet( sheet);

        ReportProcessor processor = new ReportProcessor();
        processor.process( book);
    }

    @Test
    public void パターン3() throws Exception {
        ReportBook book = new ReportBook( getTemplateUrl(), "Pattern3output", ExcelExporter.FORMAT_TYPE);
        ReportSheet sheet = createReportSheet( "Pattern3");
        book.addReportSheet( sheet);

        ReportProcessor processor = new ReportProcessor();

        // Sheet[Pattern3] Cell[1,0] Message[java.lang.IllegalArgumentException: There are crossing merged regions in the range.]
        // Cause[java.lang.IllegalArgumentException: There are crossing merged regions in the range.]
        processor.process( book);
    }

    @Test
    public void パターン3改変() throws Exception {
        ReportBook book = new ReportBook( getTemplateUrl(), "Pattern3-2output", ExcelExporter.FORMAT_TYPE);
        ReportSheet sheet = createReportSheet( "Pattern3-2");
        book.addReportSheet( sheet);

        ReportProcessor processor = new ReportProcessor();
        processor.process( book);
    }

}
