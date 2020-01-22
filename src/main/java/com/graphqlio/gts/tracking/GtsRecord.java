/*******************************************************************************
 * *
 * **  Design and Development by msg Applied Technology Research
 * **  Copyright (c) 2019-2020 msg systems ag (http://www.msg-systems.com/)
 * **  All Rights Reserved.
 * ** 
 * **  Permission is hereby granted, free of charge, to any person obtaining
 * **  a copy of this software and associated documentation files (the
 * **  "Software"), to deal in the Software without restriction, including
 * **  without limitation the rights to use, copy, modify, merge, publish,
 * **  distribute, sublicense, and/or sell copies of the Software, and to
 * **  permit persons to whom the Software is furnished to do so, subject to
 * **  the following conditions:
 * **
 * **  The above copyright notice and this permission notice shall be included
 * **  in all copies or substantial portions of the Software.
 * **
 * **  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * **  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * **  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * **  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * **  CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * **  TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * **  SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * *
 ******************************************************************************/
package com.graphqlio.gts.tracking;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.graphqlio.gts.exceptions.GtsRecordFormatException;


/**
 * GtsRecord
 *
 * @author Michael Schäfer
 * @author Dr. Edgar Müller
*/

public class GtsRecord {

	
	static final String REGEXPPATTERN = "^(?:(.+?)#(.+?)\\.(.+?)->)?(.+?)\\((.+?)\\)->(.+?)#\\{(.*?)\\}\\.\\{(.+?)\\}$";
	
		
    // Introduce GtsOperationType 
	public enum GtsOperationType {

		
		NONE {
			@Override
	        public String toString() {
	            return "";
	        }
	    },		
		READ {
			@Override
	        public String toString() {
	            return "read";
	        }
	    },		
		CREATE {
			@Override
	        public String toString() {
	            return "create";
	        }
	    },	
		UPDATE {
			@Override
	        public String toString() {
	            return "update";
	        }
	    },
		DELETE {
			@Override
	        public String toString() {
	            return "delete";
	        }
	    }
	}

	
    // Introduce GtsArityType 
	public enum GtsArityType {
		
		NONE {
			@Override
	        public String toString() {
	            return "";
	        }
	    },
		ONE {
			@Override
	        public String toString() {
	            return "one";
	        }
	    },
		MANY {
			@Override
	        public String toString() {
	            return "many";
	        }
	    },
		ALL {
			@Override
	        public String toString() {
	            return "all";
	        }
	    }

	}
		
	
	private String srcType = null; 
	private String srcId = null; 
	private String srcAttr = null; 
	private GtsOperationType op = GtsOperationType.READ; 	
	private GtsArityType arity = GtsArityType.ONE;
	private String dstType = "";
	private String dstIds[] = null;
	private String dstAttrs[] = null;


    // Introduce Getter;  Setter not required, as we use Builder 

	public String srcType() {
		return srcType; 
	}
	
	public String srcId() {
		return srcId; 
	}
	
	public String srcAttr() {
		return srcAttr; 
	}

	public GtsOperationType op() {
		return op; 
	}
	
	public GtsArityType arity() {
		return arity; 
	}

	public String dstType() {
		return dstType; 
	}
	
	public String[] dstIds() {
		return dstIds; 
	}
	
	public String[] dstAttrs() {
		return dstAttrs; 
	}

		
	@Override
	public String toString() {
		return 	"<GtsRecord> (" + 
				" srcType = " + srcType + 
				", srcId = " + srcId + 
				", srcAttr = " + srcAttr + 
				", op = " + op + 
				", arity = " + arity + 
				", dstType = " + dstType + 
				", dstAttrs = " + "[" + String.join(",", dstAttrs) + "]" +
				", dstIds = " + "[" + String.join(",", dstIds) + "]" +
				")";
	}
			
	@Override
	public boolean equals( Object o) {
		if (this == o) return true;	/// reference to same object
		if ( !(o instanceof GtsRecord) ) return false;
		GtsRecord r = (GtsRecord)o;
		return 	( 	Objects.equals(this.srcType,r.srcType)	&& 
					Objects.equals(this.srcId, r.srcId)		&&  
					Objects.equals(this.srcAttr, r.srcAttr)	&&
					Objects.equals(this.op.toString(), r.op.toString())	&&
					Objects.equals(this.arity.toString(), r.arity.toString())		&&
					Objects.equals(this.dstType, r.dstType)	&&
					Arrays.equals(this.dstAttrs, r.dstAttrs) &&
					Arrays.equals(this.dstIds, r.dstIds)	
				);
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		
		result = 31 * result + (srcType != null ? srcType.hashCode() : 0);
		result = 31 * result + (srcId != null ? srcId.hashCode() : 0);
		result = 31 * result + (srcAttr != null ? srcAttr.hashCode() : 0);
		result = 31 * result + (op != null ? op.hashCode() : 0);
		result = 31 * result + (arity != null ? arity.hashCode() : 0);
		result = 31 * result + (dstType != null ? dstType.hashCode() : 0);
		result = 31 * result + (dstAttrs != null ? dstAttrs.hashCode() : 0);
		result = 31 * result + (dstIds != null ? dstIds.hashCode() : 0);
		
		return result;
	}
		
	
	//// stringify GtsRecord	
	//// sample format: srcType#srcId.srcAttr->op(arity)->dstType#{dstId1,dstId2}.{dstAttr1,dstAttr2}
	public String stringify() {
		String str = "";
		
		/// optional attributes
		/// srcType#srcId.srcAttr
		if ( srcType != null &&  srcId != null  &&  srcAttr != null) {
			str += srcType + "#" + srcId + "." + srcAttr + "->";
		}
		
		/// manadatory attributes
		/// op(arity)->dstType#{dstId1,dstId2}.{dstAttr1,dstAttr2}
		str += op.toString() + "(" + arity.toString() + ")->";
		str += dstType + "#";

		/// comma separated array of dstIds
		str += "{" + (dstIds != null  &&  dstIds.length > 0 ? String.join(",", dstIds) : "") + "}";
		str += ".";
		/// comma separated array of dstAttrs
		str += "{" + (dstAttrs != null  &&  dstAttrs.length > 0 ? String.join(",",dstAttrs) : "") + "}";
		
		return str;
	}

	/// check if String matches predefined regular Expression
	public static boolean matchesPredefinedPattern(String strRecord) {
		return strRecord.matches(REGEXPPATTERN);		
	}
	
	
	// Introduce Builder Pattern 
	private GtsRecord (GtsRecordBuilder builder) {

		this.srcType = builder.srcType;
		this.srcId = builder.srcId;
		this.srcAttr = builder.srcAttr;
		this.op = builder.op;
		this.arity = builder.arity;
		this.dstType = builder.dstType;
		this.dstAttrs = builder.dstAttrs;
		this.dstIds = builder.dstIds;
	}
	
    public static GtsRecordBuilder builder() {
		return new GtsRecordBuilder();
	}

 
    /// Builder for GtsRecord
	public static final class GtsRecordBuilder {

		// optional attributes 
		private String srcType = null; 
		private String srcId = null; 
		private String srcAttr = null; 

		/// mandatory attributes
		private GtsOperationType op = GtsOperationType.READ; 	
		private GtsArityType arity = GtsArityType.ONE;
		private String dstType = "";
		private String dstIds[] = null;
		private String dstAttrs[] = null;
		
		/// create record from string
		private String stringifiedRecord = null;
	
		public GtsRecordBuilder () {	
		}
		
		public GtsRecordBuilder (String stringifiedRecord) {	
			stringified(stringifiedRecord);
		}

		/// constructor with all mandatory fields
		public GtsRecordBuilder( GtsOperationType op, GtsArityType arity, String dstType, String dstAttrs[], String dstIds[] )
		{
			this.op=op;
			this.arity=arity;
			this.dstType = dstType;
			this.dstAttrs = dstAttrs;
			this.dstIds = dstIds;			
		}

		public GtsRecordBuilder srcType(String srcType ) {
			this.srcType = srcType;
			return this;
		}

		public GtsRecordBuilder srcId(String srcId ) {
			this.srcId = srcId;
			return this;
		}

		public GtsRecordBuilder srcAttr(String srcAttr ) {
			this.srcAttr = srcAttr;
			return this;
		}
				
		public GtsRecordBuilder op(GtsOperationType op ) {
			this.op=op;
			return this;
		}

		public GtsRecordBuilder arity(GtsArityType arity ) {
			this.arity=arity;
			return this;
		}
		
		public GtsRecordBuilder dstType(String dstType ) {
			this.dstType = dstType;
			return this;
		}

		public GtsRecordBuilder dstAttrs(String dstAttrs[] ) {
			this.dstAttrs = dstAttrs;
			return this;
		}
		
		public GtsRecordBuilder dstIds(String dstIds[] ) {
			this.dstIds = dstIds;
			return this;
		}

		public GtsRecordBuilder stringified(String stringifiedRecord ) {
			this.stringifiedRecord = stringifiedRecord;
			return this;
		}

		
		public GtsRecord build() {
			
			if ( this.stringifiedRecord != null) {
				unstringify(stringifiedRecord);
			}
			return new GtsRecord(this);
		}

		
		//// unstringify from record		
		private void unstringify( String strRecord) {
			
			// use pattern defined in https://github.com/rse/graphql-tools-subscribe/blob/master/src/gts-3-evaluation.js
			if (strRecord.matches(REGEXPPATTERN)) {
			
		        Pattern pattern = Pattern.compile(REGEXPPATTERN);
		        Matcher matcher = pattern.matcher(strRecord);
		        while (matcher.find()) {
	
		        	srcType(matcher.group(1)); 
		        	srcId(matcher.group(2)); 
		        	srcAttr(matcher.group(3));
		        	op(matcher.group(4)); 	
		        	arity(matcher.group(5));
		        	dstType(matcher.group(6));
		        	dstIds(matcher.group(7).split("\\,"));
		        	dstAttrs(matcher.group(8).split("\\,"));	        	
		        }		        
			}
			else {
				throw new GtsRecordFormatException(String.format("Record (%s) does not match expected GtsRecord format", strRecord));
			}
			
		}

		private void arity(String arity) {
			if (arity.equals("one"))
				this.arity = GtsArityType.ONE;
			else if (arity.equals("many"))
				this.arity = GtsArityType.MANY;
			else if (arity.equals("all"))
				this.arity = GtsArityType.ALL;
			else
				this.arity = GtsArityType.NONE;	
		}

		private void op(String op) {
			if (op.equals("read") )
				this.op = GtsOperationType.READ;
			else if (op.equals("create") )
				this.op = GtsOperationType.CREATE;
			else if (op.equals("update") ) 
				this.op = GtsOperationType.UPDATE;
			else if (op.equals("delete") )	
				this.op = GtsOperationType.DELETE;
			else 
				this.op = GtsOperationType.NONE;			
		}
				
	}
   
}
