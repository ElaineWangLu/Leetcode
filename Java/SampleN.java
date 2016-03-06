package test;
import java.io.IOException;
import java.util.*;

public class test {
	
	public static void main(String[] args){
		int experiment = 1000;
		Map<String, Integer> re = new HashMap<String, Integer>();
		
		for( int k=0; k<experiment; k++){
			String s = new String("http://www.sogou.com/book/1.html\thttp://www.sogou.com/book/3.html\thttp://www.sogou.com/book/4.html\thttp://www.sogou.com/news/1.html\thttp://www.sogou.com/news/2.html\thttp://www.sogou.com/news/3.html\thttp://www.sogou.com/book/1.html\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\thttp://www.sogou.com/book/1.html\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\thttp://www.sogou.com/book/1.html\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\thttp://www.sogou.com/book/1.html\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\ta\tb\tc\ta\tb\t");
			int sampleN = 2;
			
			test urlCnt = new test();
			Map<String, Integer> countMap = new HashMap<String, Integer>();
			Map<String, Float> countFre = new HashMap<String, Float>();
			List<String> resultList = new ArrayList<String>(); //存放样本的List
			List<String> countList = new ArrayList<String>(); //存放源数据的List
			
			int M = 0;
			
			//计算每一个url的统计数countMap
			try{
				urlCnt.getSum(s, countMap, countList);
			}catch( IOException  e){
				System.out.println("error");
			}
			
			//计算url的总数M
			Iterator<String> iter = countMap.keySet().iterator();
			while( iter.hasNext() ){
				String temp = iter.next();
				int i = ( (Integer)countMap.get(temp) ).intValue();
				System.out.print(temp+":");System.out.println(i);
				M += i;
			}
			System.out.print("Total：");System.out.println(M);
			
			//计算每一个url的次数countFre
			iter = countMap.keySet().iterator();
			while( iter.hasNext() ){
				String temp = iter.next();
				int i = ( (Integer)countMap.get(temp) ).intValue();
				float j = (float)i/M;
				countFre.put( temp, new Float(j) );
				System.out.print(temp+":");System.out.println(j);
			}
			
			//非等概率抽样
			resultList = getSample( sampleN, countFre, countList);
			iter = resultList.iterator();
			System.out.println("Result:");
			while( iter.hasNext() ){
				 String temp = iter.next();
				 System.out.println(iter.next());
				 
				 if( re.containsKey(temp) ){
					  int tmp = ((Integer)re.get(temp)).intValue();
					  re.put( temp, new Integer(tmp+1) );
				 }else{
					  re.put( temp, new Integer(1) );
				 }
			}
			
			iter = re.keySet().iterator();
			while( iter.hasNext() ){
				String temp = iter.next();
				int i = ( (Integer)re.get(temp) ).intValue();
				float j = (float)i/experiment;
				System.out.print(temp+":");System.out.println(j);
			}
		}
		
	}
	
	public void getSum( String s, Map<String, Integer> countMap, List<String> countList) throws IOException {
		StringTokenizer st = new StringTokenizer( s );

		while ( st.hasMoreTokens() ){
			  String temp = st.nextToken();
			  //System.out.println(temp);
			  if( countMap.containsKey(temp) ){
				  int tmp = ((Integer)countMap.get(temp)).intValue();
				  countMap.put( temp, new Integer(tmp+1) );
			  }else{
				  countMap.put( temp, new Integer(1) );
				  countList.add( temp);
			  }
		}
	}
	
	public static List<String> getSample( int sampleN, Map<String, Float> countFre, List<String> countList){
		List<String> resultList = new ArrayList<String>(); //存放样本的List
		
		//产生随机数
		Random r = new Random();
		long seed = r.nextLong();
		r.setSeed(seed);
		
		//将所有值混合均匀
		for (int i = 0; i < countList.size(); ++i) {
			String tmp = countList.get(i);
			int j = r.nextInt(countList.size());
			countList.set(i, countList.get(j));
			countList.set(j, tmp);
		}
		
		//按概率抽样
		for (int i = 0; i < countList.size(); ++i) {
			String tmp = countList.get(i);
			float tempFre = ( (Float)countFre.get(tmp) ).floatValue();
			float rFloat = r.nextFloat();
			if( resultList.size()<sampleN ){
					resultList.add(tmp);
			}else{
				if( rFloat<=tempFre ){
					int index = r.nextInt(sampleN);
					if ( index!=sampleN ){
						resultList.set(index, tmp);
					}
				}
			}
		}
		
		return resultList;
	}
	
	
}
