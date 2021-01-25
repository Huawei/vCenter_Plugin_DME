import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class PerformanceService {
  constructor(private http: HttpClient) {}
  // iopsChart后台请求数据
  iopsChart = {
    height: 300,
    title: {
      text: 'IOPS',
      subtext: 'IO/s',
      textStyle: {
        fontStyle: 'normal' // y轴线消失
      },
      textAlign: 'bottom',
      // left: '120px'
    },
    xAxis: {
      type: 'category',
      data: [
        // '4:00', '6:00', '8:00', '10:00', '12:00', '14:00', '16:00', '18:00', '20:00', '22:00', '01/20'
      ]
    },
    yAxis: {
      type: 'value',
      max: 8,    // 设置最大值
      min: 0,
      splitNumber: 2,
      boundaryGap: ['50%', '50%'],
      axisLine: {
        show: false // y轴线消失
      },
    },
    tooltip: {},
    legend: {
      data: [
        {
          name: 'Upper Limit',  // 强制设置图形为圆。
          // icon: 'dotted',
        },
        {
          name: 'Lower Limit',  // 强制设置图形为圆。
          // icon: 'dottedLine',
        },
        {
          name: 'Read',  // 强制设置图形为圆。
          icon: 'triangle',
        },
        {
          name: 'Write',  // 强制设置图形为圆。
          icon: 'triangle',
        }
        ],
      y: 'top',    // 延Y轴居中
      x: 'right' // 居右显示
    },
    // dataZoom: [
    //   {   // 这个dataZoom组件，默认控制x轴。显示滑动框
    //     type: 'slider', // 这个 dataZoom 组件是 slider 型 dataZoom 组件
    //     xAxisIndex: 0, // x轴
    //     start: 10,      // 左边在 10% 的位置。
    //     end: 60         // 右边在 60% 的位置。
    //   },
    //   {   // 这个dataZoom组件，也控制x轴。 页面拖拽
    //     type: 'inside', // 这个 dataZoom 组件是 inside 型 dataZoom 组件
    //     xAxisIndex: 0, // x轴
    //     start: 10,      // 左边在 10% 的位置。
    //     end: 60         // 右边在 60% 的位置。
    //   },
    //   {
    //     type: 'slider',
    //     yAxisIndex: 0,
    //     start: 10,
    //     end: 80
    //   },
    //   {
    //     type: 'inside',
    //     yAxisIndex: 0,
    //     start: 30,
    //     end: 80
    //   }
    // ],
    series: [
      {
        name: 'Upper Limit',
        data: [
          /*{value: 4, symbol: 'none'},
          {value: 4, symbol: 'none'},
          {value: 4, symbol: 'none'},
          {value: 4, symbol: 'none'},
          {value: 4, symbol: 'none'},
          {value: 4, symbol: 'none'},
          {value: 4, symbol: 'none'},
          {value: 4, symbol: 'none'},
          {value: 4, symbol: 'none'},
          {value: 4, symbol: 'none'},
          {value: 4, symbol: 'none'}*/],
        type: 'line',
        smooth: true,
        // 普通样式。
        itemStyle: {
          normal: {
            lineStyle: {
              width: 2,
              type: 'dotted',  // 'dotted'虚线 'solid'实线
              color: '#DB2000' // 线条颜色
            }
          }
        },
        label: {
          show: true,
          // 标签的文字。
          formatter: 'This is a normal label.'
        },
      //
      //   // 高亮样式。
      //   emphasis: {
      //     itemStyle: {
      //       // 高亮时点的颜色。
      //       color: 'blue'
      //     },
      //     label: {
      //       show: true,
      //       // 高亮时标签的文字。
      //       formatter: 'This is a emphasis label.'
      //     }
      //   }
      },
      {
        name: 'Lower Limit',
        data: [
          /*{value: 3, symbol: 'none'},
          {value: 3, symbol: 'none'},
          {value: 3, symbol: 'none'},
          {value: 3, symbol: 'none'},
          {value: 3, symbol: 'none'},
          {value: 3, symbol: 'none'},
          {value: 3, symbol: 'none'},
          {value: 3, symbol: 'none'}*/],
        type: 'line',
        smooth: true,
        itemStyle: {
          normal: {
            lineStyle: {
              width: 2,
              type: 'dotted',  // 'dotted'虚线 'solid'实线
              color: '#F8E082'
            }
          }
        },
      },
      {
        name: 'Read',
        data: [
          /*{value: 7, symbol: 'none'},
          {value: 7, symbol: 'none'},
          {value: 7, symbol: 'none'},
          {value: 7, symbol: 'none'},
          {value: 7, symbol: 'none'},
          {value: 7, symbol: 'none'},
          {value: 7, symbol: 'none'},
          {value: 7, symbol: 'none'},
          {value: 7, symbol: 'none'},
          {value: 7, symbol: 'none'},
          {value: 7, symbol: 'none'},
          {value: 7, symbol: 'none'}*/],
        type: 'line',
        smooth: true,
        itemStyle: {
          normal: {
            lineStyle: {
              width: 2,
              type: 'solid',  // 'dotted'虚线 'solid'实线
              color: '#6870c4'
            }
          }
        },
      },
      {
        name: 'Write',
        data: [
          /*{value: 5, symbol: 'none'},
          {value: 5, symbol: 'none'},
          {value: 5, symbol: 'none'},
          {value: 5, symbol: 'none'},
          {value: 5, symbol: 'none'},
          {value: 5, symbol: 'none'},
          {value: 5, symbol: 'none'},
          {value: 5, symbol: 'none'},
          {value: 5, symbol: 'none'}*/],
        type: 'line',
        smooth: true,
        itemStyle: {
          normal: {
            lineStyle: {
              width: 2,
              type: 'solid',  // 'dotted'虚线 'solid'实线
              color: '#01bfa8'
            }
          }
        },
      }
    ]
  };
  iopsChartData;
  resData: {
    code: '',
    message: '',
    data: []
  };
  getIopsChart(title: string, subtext: string, objTypeId: string, indicatorIds: any[], objIds: any[], intervalParam: string, rangeParam: string, beginTime: number, endTime: number) {
    // this.iopsChart = 对象ID（卷ID）、指标ID（IOPSID）、range范围 只传
    return new Promise((resolve, reject) => {
      this.http.post('datastorestatistichistrory/vmfsvolume', {/*obj_type_id: objTypeId,*/
        indicator_ids: indicatorIds, // 指标
        obj_ids: objIds,
        interval: intervalParam,
        range: rangeParam,
        begin_time: beginTime,
        end_time: endTime })
        .subscribe((response: any) => {
          if (response.code === '200' && response.data !== null) {
            this.resData =  response.data;
            // 设置x轴
            this.setXAxisData(rangeParam, beginTime, endTime, intervalParam);
            // 上限对象
            const upperData = this.resData[objIds[0]][indicatorIds[0]];
            console.log('upperData')
            console.log(upperData)
            // 下限对象
            const lowerData = this.resData[objIds[0]][indicatorIds[1]];
            // 最大值
            let pmaxData;
            for (const key of Object.keys(upperData.max)) {
              pmaxData = Number(upperData.max[key]);
              console.log(pmaxData);
            }
            let lmaxData;
            for (const key of Object.keys(lowerData.max)) {
              lmaxData = Number(lowerData.max[key]);
              console.log(pmaxData);
            }
            // 最小值
            const pminData = upperData.min;
            const lminData = lowerData.min;
            // 平均值
            const pavgData = upperData.avg;
            const lavgData = lowerData.avg;
            // 上、下限数据
            const uppers: any[] = upperData.series;
            const lower: any[] = lowerData.series;
            // 设置标题
            this.iopsChart.title.text = title;
            // 设置副标题
            this.iopsChart.title.subtext = subtext;
            // 设置y轴最大值
            this.iopsChart.yAxis.max = (pmaxData > lmaxData ? pmaxData : lmaxData) + 2;
            console.log('this.iopsChart.yAxis.max' + this.iopsChart.yAxis.max)
            // 设置上限、均值 折线图数据
            uppers.forEach(item => {
              for (const key of Object.keys(item)) {
                // chartData.value = item[key];
                this.iopsChart.series[2].data.push({value: Number(item[key]), symbol: 'none'});
              }
              for (const key of Object.keys(pavgData)) {
                this.iopsChart.series[0].data.push({value:  Number(pavgData[key]), symbol: 'none'});
              }
            });
            // 设置下限、均值 折线图数据
            lower.forEach(item => {
              for (const key of Object.keys(item)) {
                this.iopsChart.series[3].data.push({value: Number(item[key]), symbol: 'none'});
              }
              for (const key of Object.keys(lavgData)) {
                this.iopsChart.series[1].data.push({value: Number(lavgData[key]), symbol: 'none'});
              }
            });
            resolve(this.iopsChart);
          }
        }, err => {
          console.error('ERROR', err);
        });
    });
  }
  setXAxisData(rangeParam: string, beginTime: number, endTime: number, intervalParam: string) {
    let rangeNum;
    let intervalNum;
    switch (rangeParam) {
      case 'LAST_1_HOUR': // 过去一小时
        rangeNum = 1;
        break;
      case 'LAST_4_HOUR': // 过去四小时 此值目前接口没有
        rangeNum = 4;
        break;
      case 'LAST_12_HOUR': // 过去12小时 此值目前接口没有
        rangeNum = 12;
        break;
      default: // 默认过去24h
        rangeNum = 24;
        break;
    }
    switch (intervalParam) {
      case 'ONE_MINUTE':
        intervalNum = 60;
        break;
      case 'MINUTE':
        intervalNum = 60;
        break;
      case 'HALF_HOUR':
        intervalNum = 2;
        break;
      default: // 默认小时
        intervalNum = 1;
        break;
    }
    // 开始时间
    const startDate = new Date(beginTime);
    // 结束时间
    const endDate = new Date(endTime);
    console.log('--------------')
    console.log(beginTime)
    console.log(endTime)
    console.log(startDate.getMonth())
    console.log(endDate)
    console.log(intervalNum)
    console.log(rangeNum)
    for (let i = 0; i < rangeNum * intervalNum; i++) {
      const indexTime = startDate;
      switch (intervalParam) {
        case 'ONE_MINUTE':
          indexTime.setMinutes(startDate.getMinutes() + i);
          break;
        case 'MINUTE':
          indexTime.setMinutes(startDate.getMinutes() + i);
          break;
        case 'HALF_HOUR':
          indexTime.setMinutes(startDate.getMinutes() + i * 30);
          break;
        default: // 默认小时
          indexTime.setHours(startDate.getHours() + i);
          break;
      }
      const asd = (indexTime.getHours() + ':' + indexTime.getMinutes());
      this.iopsChart.xAxis.data.push(asd);
    }
    // 设置最后的日期 月/天
    // this.iopsChart.xAxis.data.push(endDate.getMonth() + '/' + endDate.getDay());
  }
}

