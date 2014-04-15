# PDFWriter

This is a library that simplify how to create PDF reports with [PDFBox](http://pdfbox.apache.org/), providing you with easy but powerful concepts that will help you automate this kind of PDF files.

## Motivation

Standard PDF reporting is a common task in many organizations and usually Java libraries that deal with PDF are really low level, without providing the user with an standardized way to "just" create a report, so here is where PDFWriter is laying. With an inspiration coming from [JFreeReport](http://reporting.pentaho.com/), I decided to finally use a powerful baseline library like [PDFBox](http://pdfbox.apache.org/) and create simple but powerful components that will make my live easy when dealing with reports.

## Usage and Available components

Nowadays the list of available components is still not complete, but there are already a few of them. I will be adding them as soon as I need another ones, or whenever I can make new ones. Actually you can find in PDFWriter:

* A basic title page.
* A basic content page, acts as an index.
* A basic table page, can include a chart with data coming from the table.

But you can also find another sub components, already interesting enoughs or useful to make a new ones.

* Every page can have a header that will be displayed on top of it.
* You can also include, or not a page number at the bottom of a basic page.
* You can include charts, powered by [JFreeChart](http://www.jfree.org/jfreechart/). Actually [Bar](http://www.jfree.org/jfreechart/samples.html),  [Line](http://www.jfree.org/jfreechart/samples.html) and others like the custom PQ ones. In the feature we will add more, who knows.
* Some times you want to paint an updated, or rearranged, version of your data, so in PDFWriter you can do that by implementing a Reducer that whenever appended to a chart will be responsible of adapting your data to the new format on line.

And the last, but not the less important, you've an AbstractPDFWriter class that provide you with some basic functionality usefull to create the reports, obviously you can extend it, but also if you just use it it's still enoughts to have a nice pdf output.

### Usage

Here you've an example how to create a very basic PDF document with this lib, this file could be found under the examples package.

```
public class PDFRecordWriter  extends AbstractPDFWriter {

	private String filename;
	private PageHeader header; 
	

	public static final String PDF_TITLE = "pdf.title";
	public static final String PDF_TITLE_SUB = "pdf.title.sub";
	public static final String PDF_FILE_NAME = "pdf.file.name";
	
	public static final String PDF_JOB_ID = "pdf.job.id";
	public static final String PDF_JOB_NAME = "pdf.job.name";
	public static final String PDF_TIMESTAMP = "pdf.creation.date";
 	 
	
	public static void main(String[] args) throws Exception {
		Map<String, String> options = new HashMap<String, String>();
		options.put(PDF_TITLE, "PDFWriter Example");
		options.put(PDF_TITLE_SUB, "Example document");
		options.put(PDF_FILE_NAME, "pdfwriter");
		PDFRecordWriter writer = new PDFRecordWriter("example", options);
		String key = "ExampleKey";
		Random rand = new Random(System.currentTimeMillis());
		for(int i=0; i < 5; i++) {
			StringBuilder sb = new StringBuilder();
			for(int j=0; j < 4; j++) {
				if (j > 0)
					sb.append(",");
				sb.append(rand.nextDouble());
			}
			writer.write(key, sb.toString());
		}
		writer.close();
	}
	
	public PDFRecordWriter(String name, Map<String, String> options) {
		super();
  		filename = options.get(PDF_FILE_NAME)+"-"+name+".pdf";
 		
		try {
			prepareReport(new FileOutputStream(filename), false);
			
			String title = options.get(PDF_TITLE);
			String sub   = options.get(PDF_TITLE_SUB);
			options.remove(PDF_TITLE);
			options.remove(PDF_TITLE_SUB);
			options.remove(PDF_FILE_NAME);
			
			addTitlePage(title, sub , options);
 			setPageHeader(new PageHeader("Example table"));
 			addTable(true, true,  getHeadersDefinition());
 		} catch (IOException e) {
 			e.printStackTrace();
		}
	}


	
	public String[] getHeadersDefinition() {
		return new String[]{"MIN", "AVG", "StdDev", "MAX"};
	}
	
	public void write(String key, String value) throws IOException {
 		
		StreamEvent sEvent = new StreamEvent();

		String[] sValues = value.split(",");
  		for(String sValue : sValues) {
  			Double dValue = Double.valueOf(sValue.replaceAll("[\\[|\\]]", ""));
  			       dValue = Math.floor(dValue*1000000)/1000000.0;
  			sEvent.addElement(dValue.toString());
  		}
  		recordSubmited(sEvent);	
	}
	

	public void close() throws IOException {
		flushTableLegend();
		flushAggTable();
 		super.close();
	}

	@Override
	public void setPageHeader(PageHeader header) {
		this.header = header;
	}

	@Override
	public PageHeader getPageHeader() {
 		return header;
	}

	@Override
	public boolean hasToIncludeHeader() {
		return header != null;
	}
}
```

### Documentation

A part from this simple README file you can take a look to our [javadoc](pdfwriter/site/apidocs/index.html), there you're going to find a description that might be usefull whenever you want to use this library in your code. I know this is not much of documentation, but I aim to include more as soon as I can.

## Contributing

If you find a bug, want to extend or contribute some code, feel free to send a pull request or email me. All contributions are hihgly welcome.

## License

All code int this lib is available under the [MIT](LICENSE) License, PDFBox is under the [Apache License v2.0](http://www.apache.org/licenses/LICENSE-2.0).

